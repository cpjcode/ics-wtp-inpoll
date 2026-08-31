FROM node:25-alpine AS build
WORKDIR /build
COPY package.json package-lock.json* ./
RUN npm install
COPY public ./public
COPY index.html .
COPY *.config.js .
COPY src ./src
RUN npm run build

FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /build/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]