**AI Chat App**

A backend service for conversational AI, built with Spring Boot. Supports multiple LLM providers
(OpenAI, Gemini) that can be swapped **per chat session**, backed by a pluggable provider
architecture rather than a hardcoded integration.

**Features**

- Pluggable LLM providers** — OpenAI and Gemini implementations behind a common interface,
  selected at runtime via a factory. Adding a new provider means implementing one interface,
  no changes to existing code.
- Per-session provider switching** — a conversation can start on one provider and switch to
  another mid-session without losing message history.
- Stateful chat sessions** — each session tracks its own ordered message history, replayed as
  context on every new message.
- Clean REST API** — session creation, messaging, and provider switching, with request
  validation and centralized error handling.

**Tech Stack**

- Java 17
- Spring Boot (Spring Web, Spring Validation)
- `RestClient` (Spring's synchronous HTTP client) for calling OpenAI/Gemini REST APIs
- Lombok
- In-memory storage (`ConcurrentHashMap`) — no database; sessions reset on restart by design

**Architecture**
controller/   → REST endpoints, request/response mapping
service/      → ChatService (orchestration), SessionStore (in-memory persistence)
provider/     → LlmProvider interface + OpenAiProvider, GeminiProvider, LlmProviderFactory
model/        → ChatSession, ChatMessage, Role, LlmProviderType
dto/          → Request/response payloads, decoupled from internal models
config/       → Shared RestClient bean
exception/    → Custom exceptions + centralized @RestControllerAdvice error handling

The core flow: a ChatSession holds its own LlmProviderType. When a message comes in,
ChatService asks LlmProviderFactory for whichever provider that specific session is
currently configured to use, sends the full message history plus the new message, and appends
both the user message and the reply back onto the session.

**Prerequisites**

- Java 17+
- Maven (or use the included ./mvnw wrapper — no local Maven install needed)
- An [OpenAI API key](https://platform.openai.com/api-keys)
- A [Gemini API key](https://aistudio.google.com/apikey)

Setup

1. **Clone the repo**
   bash
   git clone <your-repo-url>
   cd ai-chat
  

2. **Set your API keys as environment variables** (never hardcode these in application.yml):
   bash
   export OPENAI_API_KEY="sk-..."
   export GEMINI_API_KEY="AIza..."
   

3. **Run the app** (from the same terminal session where the variables are set):
   bash
   ./mvnw spring-boot:run
   

   The app starts on http://localhost:8080.

**Create a session**

bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -d '{"provider": "GEMINI"}'

json
{
  "sessionId": "b6f1c2e4-...",
  "provider": "GEMINI"
}


**Send a message**
bash
curl -X POST http://localhost:8080/api/sessions/{sessionId}/messages \
  -H "Content-Type: application/json" \
  -d '{"message": "Explain event-driven architecture in one sentence."}'


json
{
  "reply": "Event-driven architecture is a design pattern where services communicate by..."
}

**Switch providers mid-session:**
bash
curl -X PUT http://localhost:8080/api/sessions/{sessionId}/provider \
  -H "Content-Type: application/json" \
  -d '{"provider": "OPENAI"}'

**View session history:**
bash
curl http://localhost:8080/api/sessions/{sessionId}
