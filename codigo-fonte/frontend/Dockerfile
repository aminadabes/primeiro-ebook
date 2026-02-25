# Estágio 1: Build da aplicação Angular usando Node
FROM node:20 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configuration=production

# Estágio 2: Servindo os arquivos estáticos com Nginx
FROM nginx:alpine
# Copia o build do estagio anterior para a pasta publica do Nginx
COPY --from=builder /app/dist/semantic-ui/browser /usr/share/nginx/html
# Configuração para suportar roteamento SPA do Angular
COPY nginx-custom.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
