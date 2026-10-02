<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head><meta charset="UTF-8"><title>Connexion - Gestion Clinique</title></head>
<body>
<main>
    <h1>Connexion</h1>
    <p>${errorMessage}</p>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <label for="username">Identifiant</label>
        <input id="username" name="username" type="text" required autocomplete="username">
        <label for="password">Mot de passe</label>
        <input id="password" name="password" type="password" required autocomplete="current-password">
        <button type="submit">Se connecter</button>
    </form>
</main>
</body>
</html>
