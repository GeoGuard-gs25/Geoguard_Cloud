# ⚡ GeoGuard - Energy Control API

API desenvolvida em **Spring Boot** para gerenciar usuários, contas de energia elétrica e notificações. O acesso é protegido por autenticação JWT.

---

## 🛡️ Autenticação via JWT

### 🔐 1. Registro de novo usuário

```bash
curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}'
```

> 🔁 Esse endpoint também realiza login automático e retorna o JWT no corpo da resposta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsIn..."
}
```

---

### 🔐 2. Login

```bash
curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{
  "email": "joao@email.com",
  "password": "senha123"
}'
```

> 🔑 **Copie o token JWT** retornado e utilize nas requisições abaixo, no cabeçalho:

```http
Authorization: Bearer SEU_TOKEN_AQUI
```

---

## 📦 Entidades

### 👤 User

Campos: `id`, `name`, `email`, `password`, `bills`

### ⚡ EnergyBill

Campos: `id`, `owner`, `consumoKwh`, `valorKwh`, `amount`, `month`

### 🔔 Notification

Campos: `id`, `title`, `message`, `dataEnvio`, `lida`

---

## 📮 Endpoints protegidos (exigem JWT)

### ⚡ Criar nova conta

```bash
curl -X POST http://localhost:8080/energy-bills -H "Authorization: Bearer SEU_TOKEN_AQUI" -H "Content-Type: application/json" -d '{
  "consumoKwh": 300.5,
  "valorKwh": 0.75,
  "amount": 225.38,
  "month": "2025-05"
}'
```

### 🔔 Criar nova notificação

```bash
curl -X POST http://localhost:8080/notifications -H "Authorization: Bearer SEU_TOKEN_AQUI" -H "Content-Type: application/json" -d '{
  "title": "Nova conta gerada",
  "message": "Sua conta foi emitida com sucesso.",
  "lida": false
}'
```

### ⚡ Listar contas

```bash
curl http://localhost:8080/energy-bills -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 🔔 Listar notificações

```bash
curl http://localhost:8080/notifications -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### ⚡ Atualizar conta

```bash
curl -X PUT http://localhost:8080/energy-bills/1 -H "Authorization: Bearer SEU_TOKEN_AQUI" -H "Content-Type: application/json" -d '{
  "consumoKwh": 350.0,
  "valorKwh": 0.80,
  "amount": 280.00,
  "month": "2025-06"
}'
```

### 🗑️ Deletar conta

```bash
curl -X DELETE http://localhost:8080/energy-bills/1 -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🚀 Como rodar o projeto

### ✅ Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL
- IDE (IntelliJ, Eclipse, VS Code)

---

### ▶️ Executando

```bash
./mvnw spring-boot:run
```

Acesse:

- API: http://localhost:8080

---

## 🧪 Teste com Postman ou Insomnia

1. Crie usuário via `/users` ou faça login em `/login`
2. Salve o token JWT
3. Adicione nos headers das requisições protegidas:

```
Authorization: Bearer SEU_TOKEN
```

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- JWT
- Hibernate Validator
- PostgreSQL
- Lombok

---

## 📂 Repositório do Projeto

Todos os repositórios estão disponiveis em nossa organização do github:  
📎 [Link da organização](https://github.com/GeoGuard-gs25)

## 📹 Vídeos do Projeto

- 🎥 **Apresentação da Solução GeoGuard:**  
  [Link do vídeo](https://youtu.be/YNFErko31fM?si=hFlQK_vrn-SA1UdO)

- 🎥 **Apresentação da Solução da materia de cloud:**  
  [Link do vídeo](https://youtu.be/59UMdjg8bh4)


## 🚀 Passo a Passo de Execução por cloud

### ✅ Passo 1 - Criação do Resource Group no Azure via CLI

```bash
az group create -l eastus -n rg-vm-global
```

---

### ✅ Passo 2 - Criação da VM Linux no Azure via CLI

```bash
az vm create --resource-group rg-vm-global --name vm-global --image Canonical:ubuntu-24_04-lts:minimal:24.04.202505020 --size Standard_B2s --admin-username admin_fiap --admin-password admin_fiap@123
```

---

### ✅ Passo 3 - Criação da NSG com prioridade 1010 no Azure via CLI

```bash
az network nsg rule create --resource-group rg-vm-global --nsg-name vm-globalNSG --name port_8080 --protocol tcp --priority 1010 --destination-port-range 8080
```

---

### ✅ Passo 4 - Criação da NSG com prioridade 1020 no Azure via CLI

```bash
az network nsg rule create --resource-group rg-vm-global --nsg-name vm-globalNSG --name port_80 --protocol tcp --priority 1020 --destination-port-range 80
```

---

### ✅ Passo 5 - Abrir portas necessárias ao projeto via CLI

```bash
az vm open-port --port 80 --resource-group rg-vm-global --name vm-global
```

Realizar o acesso via SSH:

```bash
ssh admin_fiap@IP
```

---

### ✅ Passo 6 - Instalar o Docker na VM via SSH

```bash
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
```

Adicionar o repositório do Docker:

```bash
echo   "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu   $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" |   sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

Instalar o Docker:

```bash
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

---

### ✅ Passo 7 - Criar uma network para correlação entre o bancod de dados oracle e aplicação java

```bash
sudo docker network create geoguard-net
```


### ✅ Passo 8 - Executar o container do banco de dados

Baixar a imagem:

```bash
sudo docker run -d \
  --name oracle-xe \
  --network geoguard-net \
  -e ORACLE_PASSWORD=oracle \
  -p 1521:1521 \
  gvenzl/oracle-xe:21-slim
```
---

### ✅ Passo 9 - Executar o container de java

Baixar a imagem:

```bash
sudo docker run --rm -d \
  --name geoguard-app \
  --network geoguard-net \
  -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//oracle-xe:1521/XEPDB1 \
  -e SPRING_DATASOURCE_USERNAME=system \
  -e SPRING_DATASOURCE_PASSWORD=oracle \
  -p 8080:8080 \
rafabezerra/geoguard-app
```
---

### ✅ Passo 10 - Excluir VM

Baixar a imagem:

```bash
az group delete --name rg-vm-global  --yes
```
---

## 👨‍💻 Autores
-  Guilherme Alves Pedroso - RM555357
-  João Vitor Silva Nascimento - RM554694
-  Rafael Souza Bezerra - 557888
