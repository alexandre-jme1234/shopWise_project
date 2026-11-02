# --- Étape 1 : build Angular (Node) ---
FROM node:20-alpine AS build
WORKDIR /usr/src/app
# dépendances d'abord : couche mise en cache tant que package*.json ne change pas
COPY package.json package-lock.json ./
ENV PUPPETEER_SKIP_DOWNLOAD=true
RUN npm ci
COPY . .
RUN npx ng build --configuration production

# --- Étape 2 : serveur web de production (nginx) ---
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /usr/src/app/dist/shop-wise-front-app/browser /usr/share/nginx/html
EXPOSE 80
