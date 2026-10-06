<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head><meta charset="UTF-8"><title>Connexion - Gestion Clinique</title></head>
<body>
<main>
    <h1>Connexion réussie</h1>
    <p>Utilisateur : ${authenticatedUser.username}</p>
    <p>Rôle : ${authenticatedUser.role}</p>
    <form method="post" action="${pageContext.request.contextPath}/logout">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit">Se déconnecter</button>
    </form>
</main>
</body>
</html>
