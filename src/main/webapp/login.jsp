<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion - Télé-Expertise Médicale</title>
</head>
<body>
    <h2>Connexion Application Clinique</h2>
    
    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="${pageContext.request.contextPath}/auth/login" method="post">
        <!-- Protection CSRF -->
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
        
        <div>
            <label>Nom d'utilisateur :</label>
            <input type="text" name="username" required/>
        </div>
        <div>
            <label>Mot de passe :</label>
            <input type="password" name="password" required/>
        </div>
        <button type="submit">Se connecter</button>
    </form>
</body>
</html>