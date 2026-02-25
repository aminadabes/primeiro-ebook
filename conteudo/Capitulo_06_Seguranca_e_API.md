# Capítulo 6: Protegendo o Conhecimento - Segurança e APIs

Em uma plataforma de curadoria acadêmica e pastoral, segurança da informação não é apenas sobre senhas, mas sobre permissões: quem pode fazer upload de um arquivo, e quem pode apenas ler um. 

## 6.1 Backend: Spring Security e JWT

A proteção de rotas moderna abandonou o uso de Sessões (Session/Cookies) mantidas na memória do servidor, porque isso quebra a escalabilidade. Em 2026, com Virtual Threads e instâncias subindo num cluster Kubernetes em segundos, o Backend precisa ser **Stateless** (sem estado).

É aqui que usamos **JSON Web Tokens (JWT)**.

O fluxo arquitetural do nosso `semantic-api`:
1. O Usuário envia as credenciais para o endpoint público `/api/auth/login`.
2. O Spring Security valida as credenciais contra a tabela de Usuários do PostgreSQL.
3. Se válido, geramos um Token criptografado assinado com uma chave secreta e devolvemos ao Angular.
4. Para todas as requisições subsequentes (como "fazer upload do PDF"), o Angular envia esse token no *Header HTTP*. Se o Token for adulterado, a assinatura digital falha em décimos de milissegundo.

### Desacoplando o Security da Clean Architecture
É fundamental que as nossas Entities em `core/domain` não importem pacotes como `org.springframework.security`. A autenticação acontece estritamente na borda do sistema (nas pastas `web/api` e `infrastructure/security`). Quando a execução atinge os Use Cases, o token já foi validado e convertido em um escopo de permissão abstrato.

## 6.2 Frontend: A Barreira Invisível com Angular

No frontend Angular 18, controlamos a segurança de duas fontes: Guards e Interceptors.

### Interceptors Funcionais
No passado, criaríamos classes implementando interfaces para interceptar tráfego. Hoje passamos uma função de interceptação diretamente na inicialização do `appConfig`! 
O papel desse interceptor é "sequestrar" toda requisição saindo da aplicação, anexar o `Bearer {Token JWT}` escondido nela, e deixá-la voar pela rede. E se o servidor devolver um código `401 Unauthorized`? O interceptor captura a resposta antes do Componente e atira o usuário de volta para a tela de Login.

```typescript
// Exemplo puro e moderno (Interceptor Angular 18+)
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('jwt_auth');
  
  if (token) {
    const clonedReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(clonedReq);
  }
  
  return next(req);
};
```

### Guards Reativos
Para evitar que uma tela de conteúdo seja baixada se o usuário desligar a internet e tentar hackear a URL, o Angular usa **Router Guards** (`CanActivateFn`).
Ele checa o Signal do Serviço de Autenticação: "O Signal aponta que ele está logado?". Se a resposta for sim, a rota carrega. Se não, o carregamento do módulo daquele Componente Standalone nem sequer é requisitado. Zero bytes desperdiçados!

---
Nas próximas páginas detalharemos a configuração do WebSecurityConfigurer e dos filtros Jwt do Java.
