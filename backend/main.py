import psycopg2
from flask import Flask, jsonify, request

app = Flask(__name__)

def ejecutar_sql(sql_text, params=None, es_insert=False):
    host = "localhost"
    port = "5432"
    dbname = "alexsoft"
    user = "postgres"
    password = "postgres"

    connection = psycopg2.connect(
        host=host,
        port=port,
        dbname=dbname,
        user=user,
        password=password,
    )
    cursor = connection.cursor()

    if params:
        cursor.execute(sql_text, params)
    else:
        cursor.execute(sql_text)

    if es_insert:
        connection.commit()
        cursor.close()
        connection.close()
        return jsonify({'msg': 'Operación realizada correctamente'})

    columnas = [desc[0] for desc in cursor.description]
    resultados = cursor.fetchall()
    datos = [dict(zip(columnas, fila)) for fila in resultados]

    cursor.close()
    connection.close()

    return jsonify(datos)


# ======================= RUTAS =======================

# Obtener todos los usuarios
@app.route('/usuarios', methods=['GET'])
def obtener_usuarios():
    return ejecutar_sql('SELECT * FROM "Usuario" ORDER BY id_usuario')


# Obtener todos los roles
@app.route('/roles', methods=['GET'])
def obtener_roles():
    return ejecutar_sql('SELECT * FROM "Rol" ORDER BY id_rol')


# Obtener doctores con clínica y especialidad
@app.route('/doctores', methods=['GET'])
def obtener_doctores():
    return ejecutar_sql('''
        SELECT d.id_doctor, u.nombre AS nombre_doctor, c.nombre AS clinica, e.nombre AS especialidad
        FROM "Doctor" d
        JOIN "Usuario" u ON d.id_usuario = u.id_usuario
        JOIN "Clinica" c ON d.id_clinica = c.id_clinica
        JOIN "Doctor_Especialidad" de ON d.id_doctor = de.id_doctor
        JOIN "Especialidad" e ON de.id_especialidad = e.id_especialidad
    ''')


# Insertar nuevo usuario
@app.route('/usuario', methods=['POST'])
def insertar_usuario():
    datos = request.get_json()
    sql = '''
        INSERT INTO "Usuario" (id_usuario, nombre, email, contraseña)
        VALUES (%s, %s, %s, %s)
    '''
    params = (datos['id_usuario'], datos['nombre'], datos['email'], datos['contraseña'])
    return ejecutar_sql(sql, params, es_insert=True)


# Insertar nuevo paciente
@app.route('/paciente', methods=['POST'])
def insertar_paciente():
    datos = request.get_json()
    sql = '''
        INSERT INTO "Paciente" (id_paciente, id_usuario)
        VALUES (%s, %s)
    '''
    params = (datos['id_paciente'], datos['id_usuario'])
    return ejecutar_sql(sql, params, es_insert=True)


# Insertar nuevo doctor
@app.route('/doctor', methods=['POST'])
def insertar_doctor():
    datos = request.get_json()
    sql = '''
        INSERT INTO "Doctor" (id_doctor, id_usuario, id_clinica, horario_disponible)
        VALUES (%s, %s, %s, %s)
    '''
    params = (
        datos['id_doctor'],
        datos['id_usuario'],
        datos['id_clinica'],
        datos['horario_disponible']
    )
    return ejecutar_sql(sql, params, es_insert=True)


# Obtener disponibilidad de doctores
@app.route('/disponibilidad', methods=['GET'])
def disponibilidad_doctores():
    return ejecutar_sql('''
        SELECT d.id_doctor, u.nombre AS doctor, dd.dia_semana, dd.hora_inicio, dd.hora_fin
        FROM "Doctor" d
        JOIN "Usuario" u ON d.id_usuario = u.id_usuario
        JOIN "Disponibilidad_Doctor" dd ON d.id_doctor = dd.id_doctor
        ORDER BY dd.dia_semana, dd.hora_inicio
    ''')


# ======================= INICIO APP =======================

if __name__ == '__main__':
    app.run(debug=True)
