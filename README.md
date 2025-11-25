Este repositório contém dois projetos distintos: o backend em Java (Spring Boot) e o frontend em Angular. 
A execução completa do sistema é feita via Docker Compose.


Estrutura do Repositório:
.
├── backend/           # Projeto Java (Spring Boot)
├── frontend/          # Projeto Angular
├── docker-compose.yml # Arquivo para orquestração dos containers
└── README.md


Tecnologias:
* Backend: Java, Spring Boot, Spring Data JPA, H2
* Frontend: Angular, Angular Material
* Contêinerização: Docker, Docker Compose


Pré-requisitos:
* Docker
* Docker Compose

Certifique-se de ter o Docker e Docker Compose instalados em sua máquina.


Como executar:

1. Clone o repositório:

git clone https://github.com/gjaniski/teste_tecnico.git
cd teste_tecnico

2. Execute o Docker Compose:

docker-compose up --build

3. Acesse a aplicação:

http://localhost:4200


Observações:
Certifique-se de que as portas 4200 e 8080 estejam livres no seu ambiente.