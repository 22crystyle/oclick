# OClick

Проект разделенный на микросервисы используя gradle модули для автоматизированных откликов с системой плагинов.

## Архитектура

* **buildSrc** - *конвенциональные плагины и общие зависимости для всего проекта*
* **libs** - *библиотеки*
    * `keycloak-provider` - *SPI для брокера сообщений*
    * **provider-contracts** - *API системы плагинов*
        * `jobboard-api` - *API для управления агрегатором вакансий*
        * `llm-api` - *API для выбора llm-backend'а*
        * `vectordb-api` - *API для удобной смены векторной бд*
    * `shared` - *Общие DTO/exceptions/security utils*
* **platform** - *Сервисы связующее платформу (Инфраструктура)*
    * `api-gateway` - *Gateway для обращения сервисов*
    * `config-server` - *Общая конфигурация*
    * `eureka-server` - *Service Discovery*
* **providers** - *Реализации системы плагинов*
    * **jobboards**
        * `hh-provider`
        * `superjob-provider`
    * **llm-backends**
        * `ollama-provider`
    * **vectordb**
        * `qdrant-provider`
* **services** - *Сервисы управления логикой приложения*
    * `ai-service` - *Векторизация чанков, сохранение в VectorDB, создание сопроводительного письма, LLM-анализ.*
    * `user-profile` - *Данные пользователя (токены, вакансии, подписки)*
    * `vacancy-service` - *Получение данных (через Jobboard API), очистка, разделение на чанки, отправка отклика.*
    * `workflow-orchestrator` - *Управляет последовательностью вызовов между другими сервисами (и cron задачей)*
* `shell` - *Подобие frontend'a*

## Key Technologies

* **Backend:** Java 21, Spring Boot, Spring Cloud (Eureka, Gateway)
* **Build Tool:** Gradle with custom convention plugins
* **API Documentation:** OpenAPI (Swagger)
* **Containerization:** Docker
* **AI:** Integrations with LLMs (Ollama) and vector databases (Qdrant)

## Workflow

```mermaid
sequenceDiagram
    title Рабочий процесс обработки вакансий

    participant SM as Subscription Manager
    participant Q as User Queue (Kafka/Redis)
    participant WO as Workflow Orchestrator
    participant UP as User Profile
    participant VP as Vacancy Processor
    participant HH as HH Provider (Impl)
    participant AI as AI Service
    
    note over SM: Запуск по расписанию (Cron)
    
    SM->>Q: Message: StartProcess(U123, [V_ids])
    
    par Параллельная обработка пользователей
        Q->>WO: Read Message (U123)
        activate WO
        
        note over WO, UP: 1. Лок и данные пользователя
        WO->>UP: GET /user/U123/tokens
        activate UP
        UP-->>WO: Токены (Encrypted)
        deactivate UP
        
        note over WO, VP: 2. Агрегация и очистка (Синхронно)
        WO->>VP: POST /process/vacancies(U123, [V_ids])
        activate VP
        VP->>HH: Call API (Uses Jobboard API)
        HH-->>VP: Raw Vacancy Data
        VP-->>WO: Processed Vacancy DTOs
        deactivate VP
        
        note over WO, AI: 3. Цикл LLM-обработки (Последовательно)
        loop Для каждой вакансии (V_i)
            WO->>AI: POST /analyze/Vi (Синхронно)
            activate AI
            AI->>AI: **Контроль LLM-ресурса** (Semaphore)
            AI->>AI: LLM: JSON/Chunking/Embedding
            AI-->>WO: 200 OK
            deactivate AI
        end
        
        note over WO, VP: 4. Отправка отклика
        WO->>VP: POST /send/response(U123)
        activate VP
        VP-->>WO: 200 OK
        deactivate VP
        
        note over WO: Снять Redis-лок U123
        deactivate WO
    and
        Q->>WO: Read Message (U321)
        activate WO
        note over WO: Лок U321 и параллельная работа...
        deactivate WO
    end
```
