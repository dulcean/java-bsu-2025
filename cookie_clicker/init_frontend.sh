#!/bin/bash

set -e

mkdir -p frontend

cat > frontend/index.html <<'EOF'
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Cookie Clicker</title>
</head>
<body>
    <h1>🍪 Cookie Clicker</h1>
    <img src="cookie.png" width="200" onclick="clickCookie()" style="cursor:pointer;">
    <p id="count">Кликов: 0</p>

    <script>
        function clickCookie() {
            fetch("http://localhost:8080/api/cookie/click", { method: "POST" })
                .then(r => r.text())
                .then(c => document.getElementById("count").innerText = "Кликов: " + c);
        }
    </script>
</body>
</html>
EOF

cat > frontend/spy.html <<'EOF'
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Spy Cookie</title>
</head>
<body>
    <h2>🕵️ Шпионская страница</h2>
    <button onclick="spy()">Украсть клик</button>
    <p id="status"></p>

    <script>
        function spy() {
            fetch("http://localhost:8080/api/cookie/click", { method: "POST" })
                .then(() => status.innerText = "CORS разрешил")
                .catch(() => status.innerText = "CORS заблокировал");
        }
    </script>
</body>
</html>
EOF

echo "Frontend created successfully"

