# Aplicação de Upload para S3

## Descrição
Esta aplicação Java utiliza Spring Boot para fazer upload de arquivos para o Amazon S3. A aplicação permite listar arquivos de um diretório local e fazer o upload desses arquivos para um bucket S3.

## Tecnologias Utilizadas
- Java
- Spring Boot
- Maven
- Amazon S3 SDK

## Configuração

### Pré-requisitos
- Java 11 ou superior
- Maven
- Credenciais AWS configuradas

### Configuração do `application.yml`
Configure o arquivo `src/main/resources/application.yml` com suas credenciais AWS e outras configurações necessárias:

```yaml
spring:
  main:
    web-application-type: none
aws:
  s3:
    bucket: ${BUCKET_NAME}
    region: ${REGION}
    endpoint: https://s3.amazonaws.com/
    path-style-access: true
    access-key: ${AWS_ACCESS_KEY_ID}
    secret-key: ${AWS_SECRET_ACCESS_KEY}
    aws-session-token: ${AWS_SESSION_TOKEN}
br.com.fiap.soat7:
  step: ${STEP}
  redis-mid-url: ${REDIS_MID_URL}/redis-message/
  upload:
    s3-folder: videos/%s/%s/%s/
    dir: ${SHARED_DISK}/%s/%s/%s/
server:
  port: 8080
```

Variáveis de Ambiente
BUCKET_NAME: Nome do bucket S3.
REGION: Região do bucket S3.
AWS_ACCESS_KEY_ID: Chave de acesso AWS.
AWS_SECRET_ACCESS_KEY: Chave secreta AWS.
AWS_SESSION_TOKEN: Token de sessão AWS (opcional).
STEP: Passo de configuração (personalizado).
REDIS_MID_URL: URL do Redis Middleware.
SHARED_DISK: Caminho do disco compartilhado.

Estrutura do Projeto
src/main/java/br/com/fiap/soat7/infrastructure/config/DiskUtils.java: Classe utilitária para listar arquivos de um diretório como MultipartFile.
src/main/java/br/com/fiap/soat7/infrastructure/config/FileToMultipartFile.java: Implementação de MultipartFile para arquivos locais.
src/main/java/br/com/fiap/soat7/infrastructure/s3/S3Uploader.java: Classe responsável por fazer o upload de arquivos para o S3.


Executando a Aplicação
Para compilar e executar a aplicação, utilize os seguintes comandos:

mvn clean install
mvn spring-boot:run