import psycopg2
from flask import Flask, jsonify, request
import bcrypt
import cloudinary.uploader
import os
import jwt as pyjwt
from datetime import datetime, timedelta, timezone
from dotenv import load_dotenv

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

        print("SQL ERROR:", sql_text)

        print("PARAMS:", params)

        print("ERROR:", e)

        return jsonify({'error': str(e)}), 500

    finally:
        cursor.close()
        connection.close()



# ======================= RUTAS =======================

# ======================= LOGIN =======================
@app.route('/usuario/login', methods=['POST'])
def login_usuario():
    datos = request.get_json()
    print(f"datos {datos}")

    if 'usuario' not in datos or 'contraseña' not in datos:
        return jsonify({'error': 'Usuario y contraseña son obligatorios'}), 400

    try:
        conexion = psycopg2.connect(
            host="localhost", port="5432", dbname="fabioapi", user="alumno1234", password="Alumno1234"
        )
        cursor = conexion.cursor()

        cursor.execute('''
            SELECT id_usuario, nombre, email, usuario, contraseña, 
                   fecha_nacimiento, telefono, telefono_emergencia, 
                   alergias, antecedentes_familiares, condiciones_pasadas, 
                   procedimientos_quirurgicos, rol
            FROM Usuario
            WHERE usuario = %s
        ''', (datos['usuario'],))

        usuario_encontrado = cursor.fetchone()
        cursor.close()
        conexion.close()

        if usuario_encontrado is None:
            return jsonify({'error': 'Usuario no encontrado'}), 404

        (
            id_usuario, nombre, email, usuario, contrasena_hash,
            fecha_nacimiento, telefono, telefono_emergencia,
            alergias, antecedentes_familiares, condiciones_pasadas,
            procedimientos_quirurgicos, rol
        ) = usuario_encontrado

        # Verificar contraseña
        if bcrypt.checkpw(datos['contraseña'].encode('utf-8'), contrasena_hash.encode('utf-8')):
            load_dotenv()
            SECRET_KEY = os.getenv("SECRET_KEY")
            if not SECRET_KEY:
                return jsonify({'error': 'SECRET_KEY no definido en .env'}), 505

            exp_time = datetime.now(timezone.utc) + timedelta(days=7)
            token = pyjwt.encode(
                {
                    "usuarioid": id_usuario,
                    "usuario": usuario,
                    "exp": exp_time
                },
                SECRET_KEY,
                algorithm="HS256"
            )

            return jsonify({
                'msg': 'Login exitoso',
                'id_usuario': id_usuario,
                'usuario': usuario,
                'nombre': nombre,
                'email': email,
                'fecha_nacimiento': fecha_nacimiento,
                'telefono': telefono,
                'telefono_emergencia': telefono_emergencia,
                'alergias': alergias,
                'antecedentes_familiares': antecedentes_familiares,
                'condiciones_pasadas': condiciones_pasadas,
                'procedimientos_quirurgicos': procedimientos_quirurgicos,
                'token': token,
                'rol': rol,
            }), 200
        else:
            return jsonify({'error': 'Contraseña incorrecta'}), 401

    except Exception as e:
        print(str(e))
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
            return jsonify({'error': 'El email ya está registrado'}), 401

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
    contrasena_encriptada = bcrypt.hashpw(password_plano, bcrypt.gensalt()).decode('utf-8')

    # Insertar nuevo usuario
    sql = '''
        INSERT INTO Usuario (nombre, email, usuario, contraseña)
        VALUES (%s, %s, %s, %s)
    '''
    params = (
        datos['nombre'],
        datos['email'],
        datos['usuario'],
        contrasena_encriptada
    )

    try:
        return ejecutar_sql(sql, params, es_insert=True)
    except Exception as e:
        return jsonify({'error': f'Error al registrar usuario: {str(e)}'}), 500

# ======================= USUARIOS =======================
# Obtener perfil de un usuario
@app.route('/perfil/<int:id_usuario>', methods=['GET'])
def obtener_perfil(id_usuario):
    sql = '''
        SELECT id_usuario, nombre, email, usuario, fecha_nacimiento, telefono, telefono_emergencia,
               alergias, antecedentes_familiares, condiciones_pasadas, procedimientos_quirurgicos, rol
        FROM Usuario
        WHERE id_usuario = %s
    '''
    return ejecutar_sql(sql, (id_usuario,))

# ======================= CLINICAS =======================
# Ver todas las clínicas
@app.route('/clinicas', methods=['GET'])
def obtener_clinicas():
    id_usuario = request.args.get('id_usuario')

    sql = '''
        SELECT DISTINCT 
            c.*, 
            d.especialidad,
            CASE 
                WHEN uf.id_usuario IS NOT NULL THEN TRUE 
                ELSE FALSE 
            END AS "inFavourites"
        FROM Clinica c
        LEFT JOIN Doctor d ON c.id_clinica = d.id_clinica
        LEFT JOIN usuario_favorito uf 
            ON uf.id_clinica = c.id_clinica AND uf.id_usuario = %s
    '''

    return ejecutar_sql(sql, (id_usuario,))


# Obtener clínicas favoritas de un usuario
@app.route('/usuarios/<int:id_usuario>/favoritos', methods=['GET'])
def favoritos_usuario(id_usuario):
    sql = '''
        SELECT c.*
        FROM usuario_favorito uf
        JOIN clinica c ON uf.id_clinica = c.id_clinica
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

# Añadir clínica a favoritos
@app.route('/usuarios/<int:id_usuario>/favoritos/agregar', methods=['POST'])
def agregar_favorito(id_usuario):
    datos = request.get_json()
    id_clinica = datos.get('id_clinica')

    if not id_clinica:
        return jsonify({'error': 'id_clinica es requerido'}), 400

    # Verificar si ya está en favoritos
    sql_verificar = '''
        SELECT 1 FROM usuario_favorito WHERE id_usuario = %s AND id_clinica = %s
    '''
    existe = ejecutar_sql(sql_verificar, (id_usuario, id_clinica))
    if isinstance(existe, tuple):
        return existe
    if existe.get_json():
        return jsonify({'msg': 'Ya estaba en favoritos'}), 200

    # Insertar directamente en la tabla
    sql_insertar = '''
        INSERT INTO usuario_favorito (id_usuario, id_clinica)
        VALUES (%s, %s)
    '''
    return ejecutar_sql(sql_insertar, (id_usuario, id_clinica), es_insert=True)


# Eliminar clínica de favoritos
@app.route('/usuarios/<int:id_usuario>/favoritos/eliminar', methods=['DELETE'])
def eliminar_favorito(id_usuario):
    datos = request.get_json()
    id_clinica = datos.get('id_clinica')

    if not id_clinica:
        return jsonify({'error': 'id_clinica es requerido'}), 400

    sql = '''
        DELETE FROM usuario_favorito
        WHERE id_usuario = %s AND id_clinica = %s
    '''
    return ejecutar_sql(sql, (id_usuario, id_clinica), es_insert=True)


# ======================= DISPONIBILIDAD =======================
# Ver horarios completos de doctores
@app.route('/doctor/<int:id_doctor>/disponibilidad/completa', methods=['GET'])
def ver_disponibilidad_completa(id_doctor):
    sql = '''
        SELECT id_disponibilidad, fecha_inicio, fecha_fin, disponible
        FROM disponibilidad_doctor
        WHERE id_doctor = %s
        ORDER BY fecha_inicio
    '''
    return ejecutar_sql(sql, (id_doctor,))

# Ver disponibilidad en una fecha específica
@app.route('/doctor/<int:id_doctor>/disponibilidad/por-dia', methods=['GET'])
def ver_disponibilidad_por_dia(id_doctor):
    fecha_str = request.args.get("fecha")  # formato: YYYY-MM-DD

    if not fecha_str:
        return jsonify({"error": "Parámetro 'fecha' es requerido"}), 400

    try:
        # Validación segura
        from datetime import datetime
        fecha = datetime.strptime(fecha_str, "%Y-%m-%d").date()

        sql = '''
            SELECT id_disponibilidad, fecha_inicio, fecha_fin, disponible
            FROM disponibilidad_doctor
            WHERE id_doctor = %s
              AND DATE(fecha_inicio) = %s
            ORDER BY fecha_inicio
        '''
        return ejecutar_sql(sql, (id_doctor, fecha))
    except ValueError:
        return jsonify({"error": "Formato de fecha inválido, usa YYYY-MM-DD"}), 400

# Reservar una disponibilidad
from datetime import datetime
from flask import jsonify, request, current_app

@app.route('/doctor/disponibilidad/reservar', methods=['POST'])
def reservar_franja():
    datos = request.get_json()
    id_disponibilidad = datos.get('id_disponibilidad')
    id_usuario = datos.get('id_usuario')

    if not id_disponibilidad or not id_usuario:
        current_app.logger.error("Faltan datos requeridos")
        return jsonify({"error": "Faltan datos requeridos"}), 400

    try:
        # 1. Obtener la franja de disponibilidad
        sql_get_disponibilidad = '''
            SELECT fecha_inicio, id_doctor
            FROM disponibilidad_doctor
            WHERE id_disponibilidad = %s
        '''
        current_app.logger.info(f"Buscando disponibilidad para id {id_disponibilidad}")
        respuesta = ejecutar_sql(sql_get_disponibilidad, (id_disponibilidad,))

        if isinstance(respuesta, tuple) and respuesta[1] == 500:
            return respuesta

        datos_disponibilidad = respuesta.get_json()
        if not datos_disponibilidad:
            return jsonify({"error": "Franja no encontrada"}), 404

        fecha_cita_raw = datos_disponibilidad[0]['fecha_inicio']
        id_doctor = datos_disponibilidad[0]['id_doctor']

        # 2. Parsear la fecha
        fecha_cita_clean = fecha_cita_raw.replace(" GMT", "")
        fecha_cita = datetime.strptime(fecha_cita_clean, "%a, %d %b %Y %H:%M:%S").strftime("%Y-%m-%d %H:%M:%S")

        # 3. Insertar la cita
        sql_insert_cita = '''
            INSERT INTO cita (
                id_usuario, id_doctor, fecha_cita, estado,
                condiciones_pasadas, procedimientos_quirurgicos,
                alergias, antecedentes_familiares, medicamento_y_dosis,
                nota, motivo_cancelacion,
                nombre, fecha_nacimiento, telefono, telefono_emergencia
            ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        '''

        params = (
            id_usuario,
            id_doctor,
            fecha_cita,
            'Confirmado',
            '', '', '', '', '', '', '', '', '', '', ''
        )

        current_app.logger.info(f"Inserting cita con: usuario={id_usuario}, doctor={id_doctor}, fecha={fecha_cita}")
        response_cita = ejecutar_sql(sql_insert_cita, params, es_insert=True)

        return jsonify({"mensaje": "Cita reservada correctamente"}), 201

    except Exception as e:
        current_app.logger.error(f"Error en reserva: {str(e)}")
        return jsonify({"error": "Error en el servidor"}), 500

        if isinstance(response_cita, tuple) and response_cita[1] == 500:
            current_app.logger.error("Error al insertar la cita")
            return response_cita

        # 3. Marcar franja como ocupada
        sql_update_dispo = '''
            UPDATE disponibilidad_doctor
            SET disponible = FALSE
            WHERE id_disponibilidad = %s
        '''
        response_update = ejecutar_sql(sql_update_dispo, (id_disponibilidad,), es_insert=True)

        if isinstance(response_update, tuple) and response_update[1] == 500:
            current_app.logger.error("Error al actualizar la disponibilidad")
            return response_update

        return jsonify({"msg": "Cita creada y franja reservada"}), 200

    except Exception as e:
        current_app.logger.exception("Error inesperado en reservar_franja")
        return jsonify({"error": str(e)}), 500


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

# Eliminar una cita
@app.route('/citas/eliminar', methods=['DELETE'])
def eliminar_cita():
    datos = request.get_json()
    id_usuario = datos.get("id_usuario")
    id_cita = datos.get("id_cita")

    if not id_usuario or not id_cita:
        return jsonify({"error": "Se requieren id_usuario e id_cita"}), 400

    # Verifica que la cita existe y le pertenece
    sql_verificar = "SELECT fecha_cita, id_doctor FROM Cita WHERE id_cita = %s AND id_usuario = %s"
    resultado = ejecutar_sql(sql_verificar, (id_cita, id_usuario))

    if isinstance(resultado, tuple) and resultado[1] == 500:
        return resultado

    datos_cita = resultado.get_json()
    if not datos_cita:
        return jsonify({"error": "Cita no encontrada o no pertenece al usuario"}), 404

    # Obtener fecha_cita e id_doctor para buscar la franja
    fecha_cita = datos_cita[0]['fecha_cita']
    id_doctor = datos_cita[0]['id_doctor']

    # Buscar id_disponibilidad asociado
    sql_buscar_franja = '''
        SELECT id_disponibilidad FROM disponibilidad_doctor
        WHERE id_doctor = %s AND fecha_inicio = %s
    '''
    resultado_franja = ejecutar_sql(sql_buscar_franja, (id_doctor, fecha_cita))

    if isinstance(resultado_franja, tuple) and resultado_franja[1] == 500:
        return resultado_franja

    franjas = resultado_franja.get_json()
    if franjas:
        id_disponibilidad = franjas[0]['id_disponibilidad']

        # Rehabilitar la franja
        sql_update = '''
            UPDATE disponibilidad_doctor
            SET disponible = TRUE
            WHERE id_disponibilidad = %s
        '''
        response_update = ejecutar_sql(sql_update, (id_disponibilidad,), es_insert=True)

        if isinstance(response_update, tuple) and response_update[1] == 500:
            return response_update

    # Eliminar la cita finalmente
    sql_eliminar = "DELETE FROM Cita WHERE id_cita = %s"
    return ejecutar_sql(sql_eliminar, (id_cita,), es_insert=True)


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


# Obtener datos de interés
@app.route('/perfil/datos-de-interes/<int:id_usuario>', methods=['GET'])
def obtener_datos_de_interes(id_usuario):
    sql = '''
        SELECT fecha_nacimiento, telefono, telefono_emergencia, alergias,
               antecedentes_familiares, condiciones_pasadas, procedimientos_quirurgicos
        FROM Usuario
        WHERE id_usuario = %s
    '''
    return ejecutar_sql(sql, (id_usuario,))

# Editar datos de interés de un usuario
@app.route('/perfil/datos-de-interes/<int:id_usuario>', methods=['PUT'])
def editar_datos_de_interes(id_usuario):
    datos = request.get_json() or {}

    campos_validos = [
        "fecha_nacimiento",
        "telefono",
        "telefono_emergencia",
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



# ======================= DOCTORES =======================

# Ver todas los doctores
@app.route('/doctores', methods=['GET'])
def obtener_doctores():
    return ejecutar_sql('SELECT * FROM Doctor ORDER BY id_doctor')

# Citas de un médico un día específico
@app.route('/medico/citas/<int:id_doctor>', methods=['GET'])
def obtener_citas_por_dia_para_medico(id_doctor):
    fecha_str = request.args.get('fecha')  # YYYY-MM-DD
    if not fecha_str:
        return jsonify({"error": "Parámetro 'fecha' es requerido (YYYY-MM-DD)"}), 400

    try:
        fecha_inicio = datetime.strptime(fecha_str, '%Y-%m-%d')
        fecha_fin = fecha_inicio + timedelta(days=1)
    except ValueError:
        return jsonify({"error": "Formato de fecha inválido. Usa YYYY-MM-DD"}), 400

    sql = '''
        SELECT C.id_cita, C.id_usuario, C.id_doctor, C.fecha_cita, C.estado,
               U.nombre AS nombre_paciente, U.email
        FROM Cita C
        JOIN Usuario U ON C.id_usuario = U.id_usuario
        WHERE C.id_doctor = %s
        AND C.fecha_cita >= %s AND C.fecha_cita < %s
        ORDER BY C.fecha_cita ASC
    '''
    return ejecutar_sql(sql, (id_doctor, fecha_inicio, fecha_fin))


# Obtener id_usuario de un médico
@app.route('/doctor/por-usuario/<int:id_usuario>', methods=['GET'])
def obtener_id_doctor_por_usuario(id_usuario):
    sql = '''
        SELECT id_doctor FROM doctor WHERE id_usuario = %s
    '''
    return ejecutar_sql(sql, (id_usuario,))


# Cancelar cita siendo médico
@app.route('/citas/cancelar/<int:id_cita>', methods=['PUT'])
def cancelar_cita(id_cita):
    datos = request.get_json()
    motivo = datos.get("motivo_cancelacion")

    if not motivo:
        return jsonify({"error": "El campo 'motivo_cancelacion' es obligatorio"}), 400

    sql = '''
        UPDATE Cita
        SET estado = 'Cancelado',
            motivo_cancelacion = %s
        WHERE id_cita = %s
    '''

    try:
        return ejecutar_sql(sql, (motivo, id_cita), es_insert=True)
    except Exception as e:
        return jsonify({"error": f"Error cancelando cita: {str(e)}"}), 500

# Cambiar datos expediente cita
@app.route('/citas/editar-datos-medicos/<int:id_cita>', methods=['PUT'])
def editar_datos_medicos_cita(id_cita):
    datos = request.get_json() or {}

    campos_validos = [
        "condiciones_pasadas",
        "procedimientos_quirurgicos",
        "alergias",
        "antecedentes_familiares",
        "nombre",
        "fecha_nacimiento",
        "telefono",
        "telefono_emergencia",
        "medicamento_y_dosis",
        "nota"
    ]

    campos_actualizar = []
    valores = []

    for campo in campos_validos:
        if campo in datos:
            campos_actualizar.append(f"{campo} = %s")
            valores.append(datos[campo])

    if not campos_actualizar:
        return jsonify({"error": "No se proporcionaron campos válidos para actualizar"}), 400

    valores.append(id_cita)
    sql = f"""
        UPDATE Cita
        SET {', '.join(campos_actualizar)}
        WHERE id_cita = %s
    """

    return ejecutar_sql(sql, tuple(valores), es_insert=True)

# ======================= TEST CONNECTION =======================
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

# ======================= SEGUROS =======================
# Ver todos los seguros
@app.route('/seguros', methods=['GET'])
def obtener_seguros():
    sql = 'SELECT id_seguro, nombre FROM seguro ORDER BY nombre'
    return ejecutar_sql(sql)

# Ver todos los seguros aceptados de una clínica
@app.route('/clinicas/<int:id_clinica>/seguros', methods=['GET'])
def seguros_aceptados_clinica(id_clinica):
    sql = '''
        SELECT s.id_seguro, s.nombre
        FROM clinica_seguro cs
        JOIN seguro s ON cs.id_seguro = s.id_seguro
        WHERE cs.id_clinica = %s
        ORDER BY s.nombre
    '''
    return ejecutar_sql(sql, (id_clinica,))

# Obtener los seguros médicos de un usuario
@app.route('/usuarios/<int:id_usuario>/seguros', methods=['GET'])
def seguros_usuario(id_usuario):
    sql = '''
        SELECT s.id_seguro, s.nombre
        FROM usuario_seguro us
        JOIN seguro s ON us.id_seguro = s.id_seguro
        WHERE us.id_usuario = %s
        ORDER BY s.nombre
    '''
    return ejecutar_sql(sql, (id_usuario,))

# Agregar un seguro a un usuario
@app.route('/usuarios/<int:id_usuario>/seguros/agregar', methods=['POST'])
def agregar_seguro_usuario(id_usuario):
    datos = request.get_json()
    id_seguro = datos.get('id_seguro')

    if not id_seguro:
        return jsonify({'error': 'id_seguro es requerido'}), 400

    sql = '''
        INSERT INTO usuario_seguro (id_usuario, id_seguro)
        VALUES (%s, %s)
        ON CONFLICT DO NOTHING
    '''
    return ejecutar_sql(sql, (id_usuario, id_seguro), es_insert=True)

# Eliminar un seguro de un usuario
@app.route('/usuarios/<int:id_usuario>/seguros/eliminar', methods=['DELETE'])
def eliminar_seguro_usuario(id_usuario):
    datos = request.get_json()
    id_seguro = datos.get('id_seguro')

    if not id_seguro:
        return jsonify({'error': 'id_seguro es requerido'}), 400

    sql = '''
        DELETE FROM usuario_seguro
        WHERE id_usuario = %s AND id_seguro = %s
    '''
    return ejecutar_sql(sql, (id_usuario, id_seguro), es_insert=True)

# ======================= INICIO APP =======================

if __name__ == '__main__':
    app.run(debug=True)
