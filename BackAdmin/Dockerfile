from nginx:latest

copy dist /usr/share/nginx/html

expose 80

entrypoint [ "nginx", "-g", "daemon off;" ]