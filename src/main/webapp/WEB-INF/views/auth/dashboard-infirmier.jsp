<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Infirmier - Télé-Expertise</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body class="bg-slate-50 text-slate-800 font-sans min-h-screen flex flex-col">

    <!-- Header / Navbar -->
    <header class="bg-indigo-700 text-white shadow-md">
        <div class="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
            <div class="flex items-center space-x-3">
                <i class="bi bi-hospital-fill text-2xl text-indigo-200"></i>
                <h1 class="text-xl font-bold tracking-wide">Espace Infirmier</h1>
            </div>
            <div class="flex items-center space-x-4">
                <span class="text-indigo-100 text-sm">
                    <i class="bi bi-person-circle mr-1"></i> ${authenticatedUser.username}
                </span>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="inline">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <button type="submit" class="bg-indigo-800 hover:bg-indigo-900 text-xs px-3 py-2 rounded-lg transition duration-200 border border-indigo-500">
                        <i class="bi bi-box-arrow-right mr-1"></i> Déconnexion
                    </button>
                </form>
            </div>
        </div>
    </header>

    <!-- Container -->
    <main class="max-w-7xl mx-auto px-4 py-8 flex-grow w-full grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <!-- Left Column: Formulaire Admission -->
        <section class="lg:col-span-1 bg-white p-6 rounded-xl shadow-sm border border-slate-200 h-fit">
            <h2 class="text-lg font-bold text-slate-800 border-b pb-3 mb-4 flex items-center">
                <i class="bi bi-person-plus-fill text-indigo-600 mr-2"></i> Admission Patient
            </h2>

            <c:if test="${not empty errorMessage}">
                <div class="mb-4 rounded-lg border border-rose-200 bg-rose-50 p-3 text-sm text-rose-700">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <c:if test="${param.success == 'registered'}">
                <div class="mb-4 rounded-lg border border-emerald-200 bg-emerald-50 p-3 text-sm text-emerald-700">
                    Le patient et sa consultation en attente ont été enregistrés.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/nurse/patients" method="post" class="space-y-4">
                <input type="hidden" name="_csrf" value="${csrfToken}">

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Nom</label>
                    <input type="text" name="lastName" required class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none text-sm"/>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Prénom</label>
                    <input type="text" name="firstName" required class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none text-sm"/>
                </div>

                <div class="grid grid-cols-2 gap-2">
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Date Naissance</label>
                        <input type="date" name="birthDate" required class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none text-sm"/>
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">N° Sécurité Soc.</label>
                        <input type="text" name="socialSecurityNumber" required class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none text-sm"/>
                    </div>
                </div>

                <!-- 4 Signes Vitaux -->
                <div class="border-t pt-3 mt-3">
                    <p class="text-xs font-bold text-slate-500 uppercase mb-3">Signes Vitaux</p>
                    <div class="grid grid-cols-2 gap-3">
                        <div>
                            <label class="block text-xs text-slate-600 mb-1">Tension (mmHg)</label>
                            <input type="text" name="bloodPressure" placeholder="12/8" required class="w-full px-3 py-1.5 border rounded-lg text-sm"/>
                        </div>
                        <div>
                            <label class="block text-xs text-slate-600 mb-1">Fréq. Cardiaque (BPM)</label>
                            <input type="number" name="heartRate" placeholder="75" required class="w-full px-3 py-1.5 border rounded-lg text-sm"/>
                        </div>
                        <div>
                            <label class="block text-xs text-slate-600 mb-1">Température (°C)</label>
                            <input type="number" step="0.1" name="temperature" placeholder="37.0" required class="w-full px-3 py-1.5 border rounded-lg text-sm"/>
                        </div>
                        <div>
                            <label class="block text-xs text-slate-600 mb-1">Fréq. Respiratoire</label>
                            <input type="number" name="respiratoryRate" placeholder="16" required class="w-full px-3 py-1.5 border rounded-lg text-sm"/>
                        </div>
                    </div>
                </div>

                <button type="submit" class="w-full mt-4 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2.5 rounded-lg shadow transition duration-200 text-sm">
                    Enregistrer le Patient
                </button>
            </form>
        </section>

        <!-- Right Column: Liste des Patients du jour -->
        <section class="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-slate-200">
            <div class="flex justify-between items-center border-b pb-3 mb-4">
                <h2 class="text-lg font-bold text-slate-800 flex items-center">
                    <i class="bi bi-people-fill text-indigo-600 mr-2"></i> Patients Admis Aujourd'hui
                </h2>
                <span class="bg-indigo-100 text-indigo-800 text-xs font-bold px-2.5 py-1 rounded-full">
                    ${fn:length(patientsToday)} Patient(s)
                </span>
            </div>

            <div class="overflow-x-auto">
                <table class="w-full text-left text-sm text-slate-600">
                    <thead class="bg-slate-100 text-xs uppercase text-slate-500 font-semibold">
                        <tr>
                            <th class="p-3">Patient</th>
                            <th class="p-3">N° SS</th>
                            <th class="p-3">Signes Vitaux</th>
                            <th class="p-3">Arrivée</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        <c:forEach var="p" items="${patientsToday}">
                            <tr class="hover:bg-slate-50 transition">
                                <td class="p-3 font-semibold text-slate-800">${p.firstName} ${p.lastName}</td>
                                <td class="p-3">${p.socialSecurityNumber}</td>
                                <td class="p-3 text-xs">
                                    <span class="inline-block bg-slate-100 px-2 py-0.5 rounded mr-1">TA: ${p.bloodPressure}</span>
                                    <span class="inline-block bg-slate-100 px-2 py-0.5 rounded mr-1">FC: ${p.heartRate}</span>
                                    <span class="inline-block bg-slate-100 px-2 py-0.5 rounded mr-1">T°: ${p.temperature}°C</span>
                                    <span class="inline-block bg-slate-100 px-2 py-0.5 rounded">FR: ${p.respiratoryRate}</span>
                                </td>
                                <td class="p-3 text-xs text-slate-400">${p.arrivedAt}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty patientsToday}">
                            <tr>
                                <td colspan="4" class="text-center py-8 text-slate-400 italic">
                                    Aucun patient admis pour le moment aujourd'hui.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>

    </main>

</body>
</html>
