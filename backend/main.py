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

# ======================= PACIENTES =======================

# Registro de paciente
@app.route('/paciente/registro', methods=['POST'])
def registrar_paciente():
    datos = request.get_json()

    sql_usuario = '''
        INSERT INTO "Usuario" (id_usuario, nombre, email, contraseña)
        VALUES (%s, %s, %s, %s)
    '''
    sql_paciente = '''
        INSERT INTO "Paciente" (id_paciente, id_usuario)
        VALUES (%s, %s)
    '''

    try:
        ejecutar_sql(sql_usuario, (datos['id_usuario'], datos['nombre'], datos['email'], datos['contraseña']), es_insert=True)
        ejecutar_sql(sql_paciente, (datos['id_paciente'], datos['id_usuario']), es_insert=True)
        return jsonify({'msg': 'Paciente registrado correctamente'})
    except Exception as e:
        return jsonify({'error': str(e)})

# Inicio de sesión
@app.route('/paciente/login', methods=['POST'])
def login_paciente():
    datos = request.get_json()
    sql = '''
        SELECT * FROM "Usuario" 
        WHERE email = %s AND contraseña = %s
    '''
    resultado = ejecutar_sql(sql, (datos['email'], datos['contraseña']))
    return resultado

# Obtener perfil de paciente
@app.route('/paciente/perfil/<int:id_usuario>', methods=['GET'])
def perfil_paciente(id_usuario):
    sql = '''
        SELECT u.id_usuario, u.nombre, u.email, p.id_paciente
        FROM "Usuario" u
        INNER JOIN "Paciente" p ON u.id_usuario = p.id_usuario
        WHERE u.id_usuario = %s
    '''
    return ejecutar_sql(sql, (id_usuario,))

# Editar perfil de paciente
@app.route('/paciente/perfil/<int:id_usuario>', methods=['PUT'])
def editar_perfil(id_usuario):
    datos = request.get_json()
    sql = '''
        UPDATE "Usuario"
        SET nombre = %s, email = %s, contraseña = %s
        WHERE id_usuario = %s
    '''
    params = (datos['nombre'], datos['email'], datos['contraseña'], id_usuario)
    return ejecutar_sql(sql, params, es_insert=True)

# ======================= DOCTORES =======================

# Registro de doctor
@app.route('/doctor/registro', methods=['POST'])
def registrar_doctor():
    datos = request.get_json()

    sql_usuario = '''
        INSERT INTO "Usuario" (id_usuario, nombre, email, contraseña)
        VALUES (%s, %s, %s, %s)
    '''
    sql_doctor = '''
        INSERT INTO "Doctor" (id_doctor, id_usuario, id_clinica, horario_disponible)
        VALUES (%s, %s, %s, %s)
    '''

    try:
        ejecutar_sql(sql_usuario, (datos['id_usuario'], datos['nombre'], datos['email'], datos['contraseña']), es_insert=True)
        ejecutar_sql(sql_doctor, (datos['id_doctor'], datos['id_usuario'], datos['id_clinica'], datos['horario_disponible']), es_insert=True)
        return jsonify({'msg': 'Doctor registrado correctamente'})
    except Exception as e:
        return jsonify({'error': str(e)})

# Asignar especialidades a un doctor
@app.route('/doctor/especialidad', methods=['POST'])
def asignar_especialidad():
    datos = request.get_json()
    sql = '''
        INSERT INTO "Doctor_Especialidad" (id_doctor, id_especialidad)
        VALUES (%s, %s)
    '''
    try:
        ejecutar_sql(sql, (datos['id_doctor'], datos['id_especialidad']), es_insert=True)
        return jsonify({'msg': 'Especialidad asignada correctamente'})
    except Exception as e:
        return jsonify({'error': str(e)})

# Agregar horario de disponibilidad
@app.route('/doctor/disponibilidad', methods=['POST'])
def agregar_disponibilidad():
    datos = request.get_json()
    sql = '''
        INSERT INTO "Disponibilidad_Doctor" (id_disponibilidad, id_doctor, dia_semana, hora_inicio, hora_fin)
        VALUES (%s, %s, %s, %s, %s)
    '''
    try:
        ejecutar_sql(sql, (
            datos['id_disponibilidad'],
            datos['id_doctor'],
            datos['dia_semana'],
            datos['hora_inicio'],
            datos['hora_fin']
        ), es_insert=True)
        return jsonify({'msg': 'Disponibilidad agregada correctamente'})
    except Exception as e:
        return jsonify({'error': str(e)})

# Ver disponibilidad de un doctor
@app.route('/doctor/<int:id_doctor>/disponibilidad', methods=['GET'])
def ver_disponibilidad(id_doctor):
    sql = '''
        SELECT dia_semana, hora_inicio, hora_fin
        FROM "Disponibilidad_Doctor"
        WHERE id_doctor = %s
    '''
    return ejecutar_sql(sql, (id_doctor,))

# ======================= ADMINISTRADORES =======================

# Ver todos los usuarios
@app.route('/admin/usuarios', methods=['GET'])
def listar_usuarios():
    sql = 'SELECT * FROM "Usuario" ORDER BY id_usuario ASC'
    return ejecutar_sql(sql)

# Eliminar un usuario
@app.route('/admin/usuarios/eliminar/<int:id_usuario>', methods=['DELETE'])
def eliminar_usuario(id_usuario):
    sql = 'DELETE FROM "Usuario" WHERE id_usuario = %s'
    try:
        return ejecutar_sql(sql, (id_usuario,), es_insert=True)
    except Exception as e:
        return jsonify({'error': str(e)})

# Asignar un rol a un usuario
@app.route('/admin/usuarios/rol', methods=['POST'])
def asignar_rol():
    datos = request.get_json()
    sql = '''
        INSERT INTO "Usuario_Rol" (id_usuario, id_rol)
        VALUES (%s, %s)
    '''
    try:
        return ejecutar_sql(sql, (datos['id_usuario'], datos['id_rol']), es_insert=True)
    except Exception as e:
        return jsonify({'error': str(e)})

# Ver todas las disponibilidades de doctores
@app.route('/admin/disponibilidad', methods=['GET'])
def ver_disponibilidades():
    sql = '''
        SELECT d.id_doctor, u.nombre AS doctor, dia_semana, hora_inicio, hora_fin
        FROM "Disponibilidad_Doctor" dd
        JOIN "Doctor" d ON dd.id_doctor = d.id_doctor
        JOIN "Usuario" u ON d.id_usuario = u.id_usuario
        ORDER BY d.id_doctor, dia_semana
    '''
    return ejecutar_sql(sql)

# Editar disponibilidad de un doctor
@app.route('/admin/disponibilidad/editar', methods=['PUT'])
def editar_disponibilidad():
    datos = request.get_json()
    sql = '''
        UPDATE "Disponibilidad_Doctor"
        SET dia_semana = %s, hora_inicio = %s, hora_fin = %s
        WHERE id_disponibilidad = %s
    '''
    try:
        return ejecutar_sql(sql, (
            datos['dia_semana'], datos['hora_inicio'], datos['hora_fin'], datos['id_disponibilidad']
        ), es_insert=True)
    except Exception as e:
        return jsonify({'error': str(e)})

# Eliminar disponibilidad
@app.route('/admin/disponibilidad/eliminar/<int:id_disponibilidad>', methods=['DELETE'])
def eliminar_disponibilidad(id_disponibilidad):
    sql = 'DELETE FROM "Disponibilidad_Doctor" WHERE id_disponibilidad = %s'
    try:
        return ejecutar_sql(sql, (id_disponibilidad,), es_insert=True)
    except Exception as e:
        return jsonify({'error': str(e)})

# ======================= BUSCAR DOCTORES (PACIENTES) =======================

# Buscar doctores por especialidad
@app.route('/pacientes/buscar/especialidad', methods=['GET'])
def buscar_por_especialidad():
    especialidad = request.args.get('nombre')  # ejemplo: Cardiología
    sql = '''
        SELECT d.id_doctor, u.nombre AS doctor, e.nombre AS especialidad
        FROM "Doctor" d
        JOIN "Usuario" u ON d.id_usuario = u.id_usuario
        JOIN "Doctor_Especialidad" de ON d.id_doctor = de.id_doctor
        JOIN "Especialidad" e ON de.id_especialidad = e.id_especialidad
        WHERE LOWER(e.nombre) = LOWER(%s)
    '''
    return ejecutar_sql(sql, (especialidad,))

# Buscar doctores disponibles en cierto día y hora
@app.route('/pacientes/buscar/disponibilidad', methods=['GET'])
def buscar_por_disponibilidad():
    dia = request.args.get('dia')  # ejemplo: Lunes
    hora = request.args.get('hora')  # ejemplo: 10:00
    sql = '''
        SELECT d.id_doctor, u.nombre AS doctor, dd.dia_semana, dd.hora_inicio, dd.hora_fin
        FROM "Disponibilidad_Doctor" dd
        JOIN "Doctor" d ON dd.id_doctor = d.id_doctor
        JOIN "Usuario" u ON d.id_usuario = u.id_usuario
        WHERE dd.dia_semana = %s
          AND %s BETWEEN dd.hora_inicio AND dd.hora_fin
    '''
    return ejecutar_sql(sql, (dia, hora))

# Buscar doctores por especialidad y disponibilidad
@app.route('/pacientes/buscar/avanzado', methods=['GET'])
def buscar_por_especialidad_y_disponibilidad():
    especialidad = request.args.get('especialidad')
    dia = request.args.get('dia')
    hora = request.args.get('hora')
    sql = '''
        SELECT d.id_doctor, u.nombre AS doctor, e.nombre AS especialidad,
               dd.dia_semana, dd.hora_inicio, dd.hora_fin
        FROM "Doctor" d
        JOIN "Usuario" u ON d.id_usuario = u.id_usuario
        JOIN "Doctor_Especialidad" de ON d.id_doctor = de.id_doctor
        JOIN "Especialidad" e ON de.id_especialidad = e.id_especialidad
        JOIN "Disponibilidad_Doctor" dd ON d.id_doctor = dd.id_doctor
        WHERE LOWER(e.nombre) = LOWER(%s)
          AND dd.dia_semana = %s
          AND %s BETWEEN dd.hora_inicio AND dd.hora_fin
    '''
    return ejecutar_sql(sql, (especialidad, dia, hora))


# ======================= SOLICITUD DE CITAS =======================

# Registrar una nueva cita
@app.route('/citas/solicitar', methods=['POST'])
def solicitar_cita():
    datos = request.get_json()
    id_paciente = datos.get('id_paciente')
    id_doctor = datos.get('id_doctor')
    fecha_hora = datos.get('fecha_hora')  # formato: '2025-04-15 10:00:00'

    sql = '''
        INSERT INTO "Cita" (id_paciente, id_doctor, fecha_hora, estado)
        VALUES (%s, %s, %s, 'Pendiente')
    '''
    return ejecutar_sql(sql, (id_paciente, id_doctor, fecha_hora))


# Ver citas de un paciente
@app.route('/citas/paciente', methods=['GET'])
def citas_por_paciente():
    id_paciente = request.args.get('id_paciente')
    sql = '''
        SELECT c.id_cita, d.id_doctor, u.nombre AS doctor, c.fecha_hora, c.estado
        FROM "Cita" c
        JOIN "Doctor" d ON c.id_doctor = d.id_doctor
        JOIN "Usuario" u ON d.id_usuario = u.id_usuario
        WHERE c.id_paciente = %s
        ORDER BY c.fecha_hora
    '''
    return ejecutar_sql(sql, (id_paciente,))

# Cancelación de citas
@app.route('/citas/cancelar', methods=['POST'])
def cancelar_cita():
    datos = request.get_json()
    id_cita = datos.get('id_cita')

    sql = '''
        UPDATE "Cita"
        SET estado = 'Cancelada'
        WHERE id_cita = %s
    '''
    return ejecutar_sql(sql, (id_cita,))

# Reprogramación de citas
@app.route('/citas/reprogramar', methods=['POST'])
def reprogramar_cita():
    datos = request.get_json()
    id_cita = datos.get('id_cita')
    nueva_fecha_hora = datos.get('nueva_fecha_hora')  # formato: '2025-04-18 14:30:00'

    sql = '''
        UPDATE "Cita"
        SET fecha_hora = %s, estado = 'Pendiente'
        WHERE id_cita = %s
    '''
    return ejecutar_sql(sql, (nueva_fecha_hora, id_cita))

# ======================= NOTIFICACIONES =======================
# Crear recordatorio para una cita
@app.route('/notificaciones/recordatorio', methods=['POST'])
def crear_recordatorio():
    datos = request.get_json()
    id_usuario = datos.get('id_usuario')
    mensaje = datos.get('mensaje')
    fecha_hora = datos.get('fecha_hora')  # Ej: '2025-04-18 09:00:00'

    sql = '''
        INSERT INTO "Notificacion" (id_usuario, mensaje, fecha_hora, tipo)
        VALUES (%s, %s, %s, 'Recordatorio')
    '''
    return ejecutar_sql(sql, (id_usuario, mensaje, fecha_hora))

# Ver notificaciones de un usuario
@app.route('/notificaciones/usuario', methods=['GET'])
def notificaciones_usuario():
    id_usuario = request.args.get('id_usuario')

    sql = '''
        SELECT * FROM "Notificacion"
        WHERE id_usuario = %s
        ORDER BY fecha_hora
    '''
    return ejecutar_sql(sql, (id_usuario,))


# ======================= Registro de citas pasadas y próximas =======================
# Citas pasadas de un paciente
@app.route('/citas/pasadas', methods=['GET'])
def citas_pasadas():
    id_paciente = request.args.get('id_paciente')

    sql = '''
        SELECT * FROM "Cita"
        WHERE id_paciente = %s AND fecha_hora < CURRENT_TIMESTAMP
        ORDER BY fecha_hora DESC
    '''
    return ejecutar_sql(sql, (id_paciente,))

# Citas próximas de un paciente
@app.route('/citas/proximas', methods=['GET'])
def citas_proximas():
    id_paciente = request.args.get('id_paciente')

    sql = '''
        SELECT * FROM "Cita"
        WHERE id_paciente = %s AND fecha_hora >= CURRENT_TIMESTAMP
        ORDER BY fecha_hora
    '''
    return ejecutar_sql(sql, (id_paciente,))

# ======================= Doctores agregan notas médicas =======================
# Agregar nota médica
@app.route('/expediente/agregar', methods=['POST'])
def agregar_nota_medica():
    datos = request.get_json()
    id_paciente = datos.get('id_paciente')
    id_doctor = datos.get('id_doctor')
    nota = datos.get('nota')
    fecha_hora = datos.get('fecha_hora')  # formato: '2025-04-09 17:00:00'

    sql = '''
        INSERT INTO "Expediente" (id_paciente, id_doctor, nota, fecha_hora)
        VALUES (%s, %s, %s, %s)
    '''
    return ejecutar_sql(sql, (id_paciente, id_doctor, nota, fecha_hora))

# Ver notas médicas de un paciente
@app.route('/expediente/ver', methods=['GET'])
def ver_expediente():
    id_paciente = request.args.get('id_paciente')

    sql = '''
        SELECT * FROM "Expediente"
        WHERE id_paciente = %s
        ORDER BY fecha_hora DESC
    '''
    return ejecutar_sql(sql, (id_paciente,))


# ----------------------------------

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
