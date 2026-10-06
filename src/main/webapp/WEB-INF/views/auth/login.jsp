<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr" class="h-full bg-slate-900">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Application Clinique & Télé-Expertise</title>
    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <!-- Google Fonts Inter -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Inter', sans-serif; }
    </style>
</head>
<body class="h-full flex items-center justify-center p-4 bg-gradient-to-br from-slate-900 via-indigo-950 to-slate-900">

    <div class="w-full max-w-md">
        
        <!-- Carte de Connexion -->
        <div class="bg-slate-800/80 backdrop-blur-xl border border-slate-700/50 rounded-2xl shadow-2xl p-8 relative overflow-hidden">
            
            <!-- Élément décoratif lumineux -->
            <div class="absolute -top-12 -right-12 w-32 h-32 bg-indigo-500/20 rounded-full blur-2xl"></div>
            <div class="absolute -bottom-12 -left-12 w-32 h-32 bg-teal-500/20 rounded-full blur-2xl"></div>

            <!-- En-tête -->
            <div class="text-center mb-8 relative z-10">
                <div class="inline-flex items-center justify-center w-14 h-14 rounded-xl bg-indigo-600/20 border border-indigo-500/30 text-indigo-400 mb-3 shadow-lg shadow-indigo-500/10">
                    <i class="bi bi-hospital text-2xl"></i>
                </div>
                <h1 class="text-2xl font-bold text-white tracking-tight">Espace Médical</h1>
                <p class="text-xs text-slate-400 mt-1">Système de Télé-Expertise Clinique</p>
            </div>

            <!-- Message d'erreur (si présent) -->
            <c:if test="${not empty errorMessage or not empty param.error}">
                <div class="mb-6 p-4 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-300 text-xs flex items-center gap-3 relative z-10">
                    <i class="bi bi-exclamation-triangle-fill text-rose-400 text-base shrink-0"></i>
                    <div>
                        <p class="font-semibold">Échec d'authentification</p>
                        <p class="text-rose-400/80 mt-0.5">
                            <c:out value="${errorMessage != null ? errorMessage : 'Identifiants incorrects ou session expirée.'}"/>
                        </p>
                    </div>
                </div>
            </c:if>

            <!-- Message d'information/Succès -->
            <c:if test="${not empty param.logout}">
                <div class="mb-6 p-4 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-300 text-xs flex items-center gap-3 relative z-10">
                    <i class="bi bi-check-circle-fill text-emerald-400 text-base shrink-0"></i>
                    <span>Vous avez été déconnecté avec succès.</span>
                </div>
            </c:if>

            <!-- Formulaire de Connexion -->
            <form action="${pageContext.request.contextPath}/login" method="post" class="space-y-5 relative z-10">
                
                <!-- Jeton de protection CSRF -->
                <input type="hidden" name="_csrf" value="${csrfToken}">

                <!-- Champ Nom d'utilisateur -->
                <div>
                    <label for="username" class="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-2">
                        Nom d'utilisateur
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                            <i class="bi bi-person text-lg"></i>
                        </div>
                        <input type="text" id="username" name="username" required autocomplete="username"
                               placeholder="ex: infirmier1 ou medecin1"
                               class="w-full pl-10 pr-4 py-3 bg-slate-900/60 border border-slate-700 rounded-xl text-slate-100 placeholder-slate-500 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200">
                    </div>
                </div>

                <!-- Champ Mot de passe -->
                <div>
                    <div class="flex justify-between items-center mb-2">
                        <label for="password" class="block text-xs font-semibold text-slate-300 uppercase tracking-wider">
                            Mot de passe
                        </label>
                    </div>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                            <i class="bi bi-lock text-lg"></i>
                        </div>
                        <input type="password" id="password" name="password" required autocomplete="current-password"
                               placeholder="••••••••"
                               class="w-full pl-10 pr-10 py-3 bg-slate-900/60 border border-slate-700 rounded-xl text-slate-100 placeholder-slate-500 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition duration-200">
                        <button type="button" id="togglePassword" 
                                class="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-200 transition">
                            <i class="bi bi-eye text-lg" id="toggleIcon"></i>
                        </button>
                    </div>
                </div>

                <!-- Bouton de Soumission -->
                <button type="submit" 
                        class="w-full py-3.5 px-4 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-sm rounded-xl shadow-lg shadow-indigo-600/30 hover:shadow-indigo-500/40 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:ring-offset-slate-800 transition duration-200 flex items-center justify-center gap-2">
                    <span>Se connecter</span>
                    <i class="bi bi-arrow-right text-base"></i>
                </button>
            </form>

            <!-- Pied de carte -->
            <div class="mt-8 pt-6 border-t border-slate-700/50 text-center relative z-10">
                <p class="text-xs text-slate-500">
                    &copy; 2026 Télé-Expertise Médicale. Tous droits réservés.
                </p>
            </div>

        </div>
    </div>

    <!-- Script pour l'interaction Masquer/Afficher mot de passe -->
    <script>
        const togglePassword = document.getElementById('togglePassword');
        const passwordInput = document.getElementById('password');
        const toggleIcon = document.getElementById('toggleIcon');

        togglePassword.addEventListener('click', function () {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            
            if (type === 'text') {
                toggleIcon.classList.remove('bi-eye');
                toggleIcon.classList.add('bi-eye-slash');
            } else {
                toggleIcon.classList.remove('bi-eye-slash');
                toggleIcon.classList.add('bi-eye');
            }
        });
    </script>
</body>
</html>