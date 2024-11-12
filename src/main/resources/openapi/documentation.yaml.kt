openapi: 3.0.3
info:
  title: API de CarApp
  description: API para gestión de piezas de automóviles
  version: 1.0.0
servers:
  - url: http://localhost:8080
    description: Development server
  - url: https://localhost:8080
    description: Development server
tags:
  - name: Piezas
    description: Operaciones con piezas de automóviles
  - name: Chats
    description: Operaciones con chats
  - name: Auth
    description: Operaciones de autenticación

paths:
  /auth/signin/email:
    post:
      tags:
        - Auth
      summary: Iniciar sesión con email
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/EmailSignInRequest'
      responses:
        '200':
          description: Inicio de sesión exitoso
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaUser'
        '400':
          $ref: '#/components/responses/ErrorAutenticacion'

  /auth/signin/phone:
    post:
      tags:
        - Auth
      summary: Iniciar sesión con teléfono
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/PhoneSignInRequest'
      responses:
        '200':
          description: Inicio de sesión exitoso
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaUser'
        '400':
          $ref: '#/components/responses/ErrorAutenticacion'

  /auth/signin/oauth:
    post:
      tags:
        - Auth
      summary: Iniciar sesión con OAuth
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/OAuthSignInRequest'
      responses:
        '200':
          description: Inicio de sesión exitoso
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaUser'
        '400':
          $ref: '#/components/responses/ErrorAutenticacion'

  /auth/signup:
    post:
      tags:
        - Auth
      summary: Registrar nuevo usuario
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/SignUpRequest'
      responses:
        '201':
          description: Usuario registrado exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaUser'
        '400':
          $ref: '#/components/responses/ErrorAutenticacion'
  /auth/confirm/email:
      post:
        tags:
          - Auth
        summary: Confirmar email
        requestBody:
          required: true
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ConfirmEmailRequest'
        responses:
          '200':
            description: Usuario confirmado exitosamente
            content:
              application/json:
                schema:
                  $ref: '#/components/schemas/RespuestaUser'
          '400':
            $ref: '#/components/responses/ErrorAutenticacion'
  /auth/confirm/phone:
        post:
          tags:
            - Auth
          summary: Confirmar telefono
          requestBody:
            required: true
            content:
              application/json:
                schema:
                  $ref: '#/components/schemas/ConfirmPhoneRequest'
          responses:
            '200':
              description: Usuario confirmado exitosamente
              content:
                application/json:
                  schema:
                    $ref: '#/components/schemas/RespuestaUser'
            '400':
              $ref: '#/components/responses/ErrorAutenticacion'
  /auth/user:
    post:
      tags:
        - Auth
      summary: Actualizar información del usuario
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/UpdateUserRequest'
      responses:
        '200':
          description: Usuario actualizado exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaUser'
        '400':
          $ref: '#/components/responses/ErrorAutenticacion'
  /piezas:
    get:
      tags:
        - Piezas
      summary: Obtener todas las piezas
      responses:
        '200':
          description: Lista de piezas recuperada exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaPiezas'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

    post:
      tags:
        - Piezas
      summary: Crear una nueva pieza
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/EntradaPieza'
      responses:
        '201':
          description: Pieza creada exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaId'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

  /piezas/search:
    get:
      tags:
        - Piezas
      summary: Buscar piezas
      parameters:
        - name: query
          in: query
          required: true
          schema:
            type: string
          description: Término de búsqueda
      responses:
        '200':
          description: Resultados de la búsqueda
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaPiezas'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

  /piezas/{id}:
    parameters:
      - name: id
        in: path
        required: true
        schema:
          type: string
        description: ID de la pieza

    get:
      tags:
        - Piezas
      summary: Obtener una pieza por ID
      responses:
        '200':
          description: Pieza encontrada
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaPieza'
        '404':
          $ref: '#/components/responses/NoEncontrado'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

    put:
      tags:
        - Piezas
      summary: Actualizar una pieza
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/EntradaPieza'
      responses:
        '200':
          description: Pieza actualizada exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaMensaje'
        '404':
          $ref: '#/components/responses/NoEncontrado'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

    delete:
      tags:
        - Piezas
      summary: Eliminar una pieza
      responses:
        '200':
          description: Pieza eliminada exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaMensaje'
        '404':
          $ref: '#/components/responses/NoEncontrado'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

  /chats:
    get:
      tags:
        - Chats
      summary: Obtener todos los chats
      responses:
        '200':
          description: Lista de chats recuperada exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaChats'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

    post:
      tags:
        - Chats
      summary: Crear un nuevo chat
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/EntradaChat'
      responses:
        '201':
          description: Chat creado exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaId'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

  /chats/user/{userId}:
    get:
      tags:
        - Chats
      summary: Obtener los chats de un usuario
      parameters:
        - name: userId
          in: path
          required: true
          schema:
            type: string
          description: ID del usuario
      responses:
        '200':
          description: Lista de chats del usuario recuperada exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaChats'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

  /chats/{participantsHash}:
    get:
      tags:
        - Chats
      summary: Obtener un chat por los participantes
      parameters:
        - name: participantsHash
          in: path
          required: true
          schema:
            type: string
          description: Hash de los participantes del chat
      responses:
        '200':
          description: Chat encontrado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaChat'
        '404':
          $ref: '#/components/responses/NoEncontrado'
        '500':
          $ref: '#/components/responses/ErrorInternoServidor'

  /chats/{participantsHash}/unread:
    get:
      tags:
        - Chats
      summary: Obtener el número de mensajes no leídos en un chat
      parameters:
        - name: participantsHash
          in: path
          required: true
          schema:
            type: string
          description: Hash de los participantes del chat
      responses:
        '200':
          description: Número de mensajes no leídos obtenido exitosamente
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RespuestaNumeroNoLeidos'

components:
  schemas:
    # Schemas de autenticación
    OAuthProvider:
      type: string
      enum:
        - GOOGLE
        - FACEBOOK
        - GITHUB

    User:
      type: object
      properties:
        id:
          type: string
          description: ID del usuario
        email:
          type: string
          description: Email del usuario
          nullable: true
        phone:
          type: string
          description: Teléfono del usuario
          nullable: true
        jwt:
          type: string
          description: Token JWT del usuario

    EmailSignInRequest:
      type: object
      required:
        - email
        - password
      properties:
        email:
          type: string
        password:
          type: string

    PhoneSignInRequest:
      type: object
      required:
        - phone
        - password
      properties:
        phone:
          type: string
        password:
          type: string

    OAuthSignInRequest:
      type: object
      required:
        - provider
      properties:
        provider:
          $ref: '#/components/schemas/OAuthProvider'
        redirectUrl:
          type: string
          nullable: true

    SignUpRequest:
      type: object
      required:
        - password
      properties:
        email:
          type: string
          nullable: true
        phone:
          type: string
          nullable: true
        password:
          type: string

    UpdateUserRequest:
      type: object
      properties:
        email:
          type: string
          nullable: true
        phone:
          type: string
          nullable: true
        password:
          type: string
          nullable: true

    ConfirmEmailRequest:
         type: object
         properties:
           email:
             type: string
             nullable: true
           code:
             type: string
             nullable: true
    ConfirmPhoneRequest:
             type: object
             properties:
               phone:
                 type: string
                 nullable: true
               code:
                 type: string
                 nullable: true

    RespuestaUser:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          $ref: '#/components/schemas/User'
        message:
          type: string
          nullable: true
    Estado:
      type: string
      enum:
        - ÉXITO
        - ERROR

    Vectorizacion:
      type: object
      properties:
        palabras_clave:
          type: array
          items:
            type: string
        frases_clave:
          type: array
          items:
            type: string

    Pieza:
      type: object
      properties:
        _id:
          type: string
          description: ObjectId de MongoDB
        nombre:
          type: string
        descripcion:
          type: string
        foto:
          type: string
        modelos_compatibles:
          type: array
          items:
            type: string
        estado:
          type: string
        precio:
          type: number
          format: double
        informacion_adicional:
          type: string
        id_vendedor:
          type: integer
        vectorizacion:
          $ref: '#/components/schemas/Vectorizacion'
      required:
        - nombre
        - descripcion
        - foto
        - modelos_compatibles
        - estado
        - precio
        - informacion_adicional
        - id_vendedor
        - vectorizacion

    EntradaPieza:
      type: object
      properties:
        nombre:
          type: string
        descripcion:
          type: string
        foto:
          type: string
        modelos_compatibles:
          type: array
          items:
            type: string
        estado:
          type: string
        precio:
          type: number
          format: double
        informacion_adicional:
          type: string
        id_vendedor:
          type: integer
        vectorizacion:
          $ref: '#/components/schemas/Vectorizacion'
      required:
        - nombre
        - descripcion
        - foto
        - modelos_compatibles
        - estado
        - precio
        - informacion_adicional
        - id_vendedor
        - vectorizacion

    RespuestaPieza:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          $ref: '#/components/schemas/Pieza'
        message:
          type: string
          nullable: true

    RespuestaPiezas:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          type: array
          items:
            $ref: '#/components/schemas/Pieza'
        message:
          type: string
          nullable: true

    Mensaje:
      type: object
      properties:
        messageId:
          type: string
          description: ID del mensaje
        senderId:
          type: string
          description: ID del usuario que envió el mensaje
        contenido:
          type: string
          description: Contenido del mensaje
        timestamp:
          type: string
          description: Timestamp del mensaje
        leídoPorUsuario:
          type: boolean
          description: Indica si el mensaje ha sido leído por el usuario

    UltimoMensaje:
      type: object
      properties:
        contenido:
          type: string
          description: Contenido del último mensaje
        timestamp:
          type: string
          description: Timestamp del último mensaje
        senderId:
          type: string
          description: ID del usuario que envió el último mensaje

    Participante:
      type: object
      properties:
        userId:
          type: string
          description: ID del usuario
        username:
          type: string
          description: Nombre de usuario
        displayName:
          type: string
          description: Nombre para mostrar

    Chat:
      type: object
      properties:
        id:
          type: string
          description: ID del chat
        participantes:
          type: array
          items:
            $ref: '#/components/schemas/Participante'
          description: Lista de participantes del chat
        mensajes:
          type: array
          items:
            $ref: '#/components/schemas/Mensaje'
          description: Lista de mensajes del chat
        ultimoMensaje:
          $ref: '#/components/schemas/UltimoMensaje'
          description: Último mensaje del chat
        actualizadoEn:
          type: string
          description: Timestamp de la última actualización del chat

    EntradaChat:
      type: object
      properties:
        participantes:
          type: array
          items:
            $ref: '#/components/schemas/Participante'
          description: Lista de participantes del chat
        mensajes:
          type: array
          items:
            $ref: '#/components/schemas/Mensaje'
          description: Lista de mensajes del chat
        ultimoMensaje:
          $ref: '#/components/schemas/UltimoMensaje'
          description: Último mensaje del chat
      required:
        - participantes
        - mensajes
        - ultimoMensaje

    RespuestaChat:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          $ref: '#/components/schemas/Chat'
        message:
          type: string
          nullable: true

    RespuestaChats:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          type: array
          items:
            $ref: '#/components/schemas/Chat'
        message:
          type: string
          nullable: true

    RespuestaNumeroNoLeidos:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          type: integer
          description: Número de mensajes no leídos
        message:
          type: string
          nullable: true

    RespuestaId:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          type: string
          description: ID del objeto creado
        message:
          type: string
          nullable: true

    RespuestaMensaje:
      type: object
      properties:
        status:
          $ref: '#/components/schemas/Estado'
        data:
          type: string
        message:
          type: string
          nullable: true
  responses:
    ErrorAutenticacion:
      description: Error en la autenticación
      content:
        application/json:
          schema:
            type: object
            properties:
              status:
                $ref: '#/components/schemas/Estado'
              message:
                type: string
    NoEncontrado:
      description: Recurso no encontrado
      content:
        application/json:
          schema:
            type: object
            properties:
              status:
                $ref: '#/components/schemas/Estado'
              message:
                type: string
    ErrorInternoServidor:
      description: Error interno del servidor
      content:
        application/json:
          schema:
            type: object
            properties:
              status:
                $ref: '#/components/schemas/Estado'
              message:
                type: string