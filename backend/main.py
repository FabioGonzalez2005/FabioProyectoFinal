import psycopg2
from flask import Flask, jsonify, request
import bcrypt
import cloudinary
import cloudinary.uploader

cloudinary.config(
    cloud_name='dr8es2ate',
    api_key='418891476929184',
    api_secret='YGQKwPgfMByKjXiffUWXlP1g65k'
)

app = Flask(__name__)

def ejecutar_sql(sql_text, params=None, es_insert=False):
    import psycopg2
    from flask import jsonify

    host = "localhost"
    port = "5432"
    dbname = "fabioapi"
    user = "alumno1234"
    password = "Alumno1234"

    connection = psycopg2.connect(
        host=host,
        port=port,
        dbname=dbname,
        user=user,
        password=password,
    )
    cursor = connection.cursor()

    try:
        if params:
            cursor.execute(sql_text, params)
        else:
            cursor.execute(sql_text)

        if es_insert:
            connection.commit()
            return jsonify({'msg': 'Operación realizada correctamente'})

        # Solo intenta acceder a resultados si hay descripción de columnas (es un SELECT)
        if cursor.description:
            columnas = [desc[0] for desc in cursor.description]
            resultados = cursor.fetchall()
            datos = [dict(zip(columnas, fila)) for fila in resultados]
            return jsonify(datos)
        else:
            return jsonify({'msg': 'Operación realizada sin resultados'})

    except Exception as e:
        return jsonify({'error': str(e)}), 500

    finally:
        cursor.close()
        connection.close()



# ======================= RUTAS =======================

# ======================= LOGIN =======================
@app.route('/usuario/login', methods=['POST'])
def login_usuario():
    datos = request.get_json()

    # Validación de campos obligatorios
    if 'usuario' not in datos or 'contraseña' not in datos:
        return jsonify({'error': 'Usuario y contraseña son obligatorios'}), 400

    try:
        # Conexión directa a la base de datos
        conexion = psycopg2.connect(
            host="localhost", port="5432", dbname="fabioapi", user="alumno1234", password="Alumno1234"
        )
        cursor = conexion.cursor()

        # Buscar usuario
        cursor.execute('SELECT id_usuario, nombre, usuario, email, contraseña FROM Usuario WHERE usuario = %s', (datos['usuario'],))
        usuario_encontrado = cursor.fetchone()

        cursor.close()
        conexion.close()

        if usuario_encontrado is None:
            return jsonify({'error': 'Usuario no encontrado'}), 404

        id_usuario, nombre, email, usuario, contraseña_hash = usuario_encontrado

        # Verificar contraseña
        if bcrypt.checkpw(datos['contraseña'].encode('utf-8'), contraseña_hash.encode('utf-8')):
            return jsonify({
                'msg': 'Login exitoso',
                'id_usuario': id_usuario,
                'usuario': usuario,
                'nombre': nombre,
                'email': email
            })
        else:
            return jsonify({'error': 'Contraseña incorrecta'}), 401

    except Exception as e:
        return jsonify({'error': f'Error en el login: {str(e)}'}), 500

# ======================= REGISTER =======================
@app.route('/usuario/registro', methods=['POST'])
def registrar_usuario():
    datos = request.get_json()

    try:
        # Conexión directa a la DB para validaciones
        conexion = psycopg2.connect(
            host="localhost", port="5432", dbname="fabioapi", user="alumno1234", password="Alumno1234"
        )
        cursor = conexion.cursor()

        # Verificar si ya existe email o usuario
        cursor.execute('SELECT 1 FROM Usuario WHERE LOWER(email) = LOWER(%s)', (datos['email'],))
        if cursor.fetchone():
            cursor.close()
            conexion.close()
            return jsonify({'error': 'El email ya está registrado'}), 400

        cursor.execute('SELECT 1 FROM Usuario WHERE LOWER(usuario) = LOWER(%s)', (datos['usuario'],))
        if cursor.fetchone():
            cursor.close()
            conexion.close()
            return jsonify({'error': 'El nombre de usuario ya está en uso'}), 400

        cursor.close()
        conexion.close()
    except Exception as e:
        return jsonify({'error': f'Error al verificar duplicados: {str(e)}'}), 500

    # Encriptar contraseña
    password_plano = datos['contraseña'].encode('utf-8')
    contraseña_encriptada = bcrypt.hashpw(password_plano, bcrypt.gensalt()).decode('utf-8')

    # Insertar nuevo usuario
    sql = '''
        INSERT INTO Usuario (nombre, email, usuario, contraseña)
        VALUES (%s, %s, %s, %s)
    '''
    params = (
        datos['nombre'],
        datos['email'],
        datos['usuario'],
        contraseña_encriptada
    )

    try:
        return ejecutar_sql(sql, params, es_insert=True)
    except Exception as e:
        return jsonify({'error': f'Error al registrar usuario: {str(e)}'}), 500


# ======================= CLINICAS =======================
# Ver todas las clínicas
@app.route('/clinicas', methods=['GET'])
def obtener_clinicas():
    return ejecutar_sql('SELECT * FROM Clinica ORDER BY id_clinica')

# Obtener clínicas favoritas de un usuario
@app.route('/usuarios/<int:id_usuario>/favoritos', methods=['GET'])
def favoritos_usuario(id_usuario):
    sql = '''
        SELECT c.*
        FROM usuario_favorito uf
        JOIN favorito f ON uf.id_favorito = f.id_favorito
        JOIN clinica c ON f.id_clinica = c.id_clinica
        WHERE uf.id_usuario = %s
    '''
    return ejecutar_sql(sql, (id_usuario,))

# Buscar clínicas por especialidad:
@app.route('/clinicas/por-especialidad', methods=['GET'])
def buscar_clinicas_por_especialidad():
    from flask import request
    especialidad = request.args.get('especialidad')
    print("Especialidad recibida:", especialidad)
    if not especialidad:
        return jsonify({'error': 'Se requiere parámetro de especialidad'}), 400

    sql = '''
        SELECT DISTINCT c.*, d.especialidad
        FROM Clinica c
        JOIN Doctor d ON c.id_clinica = d.id_clinica
        WHERE LOWER(d.especialidad) LIKE LOWER(%s)
    '''
    return ejecutar_sql(sql, (f'%{especialidad}%',))

# ======================= DISPONIBILIDAD =======================
# Ver horarios de doctores
@app.route('/doctor/<int:id_doctor>/disponibilidad/completa', methods=['GET'])
def ver_disponibilidad_completa(id_doctor):
    sql = '''
        SELECT id_disponibilidad, dia_semana, hora_inicio, hora_fin, disponible
        FROM "Disponibilidad_Doctor"
        WHERE id_doctor = %s
        ORDER BY dia_semana, hora_inicio
    '''
    return ejecutar_sql(sql, (id_doctor,))

@app.route('/doctor/disponibilidad/reservar', methods=['POST'])
def reservar_franja():
    datos = request.get_json()
    id_disponibilidad = datos.get('id_disponibilidad')

    sql = '''
        UPDATE "Disponibilidad_Doctor"
        SET disponible = FALSE
        WHERE id_disponibilidad = %s
    '''
    return ejecutar_sql(sql, (id_disponibilidad,), es_insert=True)

# ======================= CITAS =======================
# Ver todas las citas por ID de usuario
@app.route('/citas', methods=['GET'])
def obtener_citas():
    from flask import request, jsonify

    id_usuario = request.args.get('id_usuario')

    if not id_usuario:
        return jsonify({'error': 'id_usuario requerido'}), 400

    sql = "SELECT * FROM Cita WHERE id_usuario = %s ORDER BY id_cita"
    return ejecutar_sql(sql, params=(id_usuario,))

# ======================= USUARIOS =======================
# Editar perfil de paciente
@app.route('/perfil/<int:id_usuario>', methods=['PUT'])
def editar_perfil(id_usuario):
    datos = request.get_json() or {}
    nombre = datos.get("nombre")
    email = datos.get("email")
    usuario = datos.get("usuario")

    connection = psycopg2.connect(
        host="localhost",
        port="5432",
        dbname="fabioapi",
        user="alumno1234",
        password="Alumno1234"
    )
    cursor = connection.cursor()

    # Verificar si el usuario existe
    cursor.execute("SELECT 1 FROM Usuario WHERE id_usuario = %s", (id_usuario,))
    if not cursor.fetchone():
        cursor.close()
        connection.close()
        return jsonify({"error": "Usuario no encontrado"}), 404

    # Validar unicidad de email
    if email:
        cursor.execute("SELECT 1 FROM Usuario WHERE email = %s AND id_usuario != %s", (email, id_usuario))
        if cursor.fetchone():
            cursor.close()
            connection.close()
            return jsonify({"error": "El email ya está en uso"}), 400

    # Validar unicidad de nombre de usuario
    if usuario:
        cursor.execute("SELECT 1 FROM Usuario WHERE usuario = %s AND id_usuario != %s", (usuario, id_usuario))
        if cursor.fetchone():
            cursor.close()
            connection.close()
            return jsonify({"error": "El nombre de usuario ya está en uso"}), 400

    # Construir actualización dinámica
    campos_actualizar = []
    valores = []

    if nombre is not None:
        campos_actualizar.append("nombre = %s")
        valores.append(nombre)
    if email:
        campos_actualizar.append("email = %s")
        valores.append(email)
    if usuario:
        campos_actualizar.append("usuario = %s")
        valores.append(usuario)

    if campos_actualizar:
        valores.append(id_usuario)
        cursor.execute(f"""
            UPDATE Usuario
            SET {', '.join(campos_actualizar)}
            WHERE id_usuario = %s
        """, tuple(valores))
        connection.commit()

    cursor.close()
    connection.close()

    return jsonify({"mensaje": "Perfil actualizado correctamente"}), 200

# Editar datos de interés de un usuario
@app.route('/perfil/datos-de-interes/<int:id_usuario>', methods=['PUT'])
def editar_datos_de_interes(id_usuario):
    datos = request.get_json() or {}

    campos_validos = [
        "fecha_nacimiento",
        "telefono",
        "contacto_emergencia",
        "alergias",
        "antecedentes_familiares",
        "condiciones_pasadas",
        "procedimientos_quirurgicos"
    ]

    # Filtrar solo los campos válidos que se hayan enviado
    campos_actualizar = []
    valores = []

    for campo in campos_validos:
        if campo in datos:
            campos_actualizar.append(f"{campo} = %s")
            valores.append(datos[campo])

    if not campos_actualizar:
        return jsonify({"error": "No se proporcionaron campos válidos para actualizar"}), 400

    # Conexión a la DB
    import psycopg2
    connection = psycopg2.connect(
        host="localhost",
        port="5432",
        dbname="fabioapi",
        user="alumno1234",
        password="Alumno1234"
    )
    cursor = connection.cursor()

    # Verificar si el usuario existe
    cursor.execute("SELECT 1 FROM Usuario WHERE id_usuario = %s", (id_usuario,))
    if not cursor.fetchone():
        cursor.close()
        connection.close()
        return jsonify({"error": "Usuario no encontrado"}), 404

    # Ejecutar UPDATE dinámico
    valores.append(id_usuario)
    sql = f"""
        UPDATE Usuario
        SET {', '.join(campos_actualizar)}
        WHERE id_usuario = %s
    """
    cursor.execute(sql, tuple(valores))
    connection.commit()

    cursor.close()
    connection.close()

    return jsonify({"mensaje": "Datos de interés actualizados correctamente"}), 200


# ======================= PACIENTES =======================

# Registro de paciente
@app.route('/paciente/registro', methods=['POST'])
def registrar_paciente():
    datos = request.get_json()

    sql_usuario = '''
        INSERT INTO Usuario (id_usuario, nombre, email, contraseña)
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

# ======================= DOCTORES =======================

# Ver todas los doctores
@app.route('/doctores', methods=['GET'])
def obtener_doctores():
    return ejecutar_sql('SELECT * FROM Doctor ORDER BY id_doctor')

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
    sql = 'SELECT * FROM Usuario ORDER BY id_usuario ASC'
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
    return ejecutar_sql('SELECT * FROM Usuario ORDER BY id_usuario')


# Obtener todos los roles
@app.route('/roles', methods=['GET'])
def obtener_roles():
    return ejecutar_sql('SELECT * FROM "Rol" ORDER BY id_rol')


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

@app.route('/test_connection', methods=['GET'])
def test_connection():
    try:
        host = "localhost"
        port = "5432"
        dbname = "fabioapi"
        user = "postgres"
        password = "Alumno1234"

        connection = psycopg2.connect(
            host=host,
            port=port,
            dbname=dbname,
            user=user,
            password=password,
        )

        cursor = connection.cursor()
        cursor.execute("SELECT 1")
        result = cursor.fetchone()
        cursor.close()
        connection.close()

        if result:
            return jsonify({"message": "Conectado a la base de datos correctamente."}), 200
        else:
            return jsonify({"message": "Error al realizar la consulta."}), 500
    except Exception as e:
            return jsonify({"message": f"Error en la conexión: {repr(e)}"}), 500


# ======================= INICIO APP =======================

if __name__ == '__main__':
    app.run(debug=True)
