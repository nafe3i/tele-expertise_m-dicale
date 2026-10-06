<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Médecin - Télé-Expertise</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body class="bg-slate-50 text-slate-800 font-sans min-h-screen flex flex-col">

    <!-- Header / Navbar -->
    <header class="bg-teal-700 text-white shadow-md">
        <div class="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
            <div class="flex items-center space-x-3">
                <i class="bi bi-file-earmark-medical-fill text-2xl text-teal-200"></i>
                <h1 class="text-xl font-bold tracking-wide">Espace Médecin Généraliste</h1>
            </div>
            <div class="flex items-center space-x-4">
                <span class="text-teal-100 text-sm">
                    <i class="bi bi-person-badge-fill mr-1"></i> Dr. ${authenticatedUser.username}
                </span>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="inline">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <button type="submit" class="bg-teal-800 hover:bg-teal-900 text-xs px-3 py-2 rounded-lg transition duration-200 border border-teal-500">
                        <i class="bi bi-box-arrow-right mr-1"></i> Déconnexion
                    </button>
                </form>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 py-8 flex-grow w-full grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <!-- Left Column: Consultations Form -->
        <section class="lg:col-span-1 bg-white p-6 rounded-xl shadow-sm border border-slate-200 h-fit">
            <h2 class="text-lg font-bold text-slate-800 border-b pb-3 mb-4 flex items-center">
                <i class="bi bi-journal-plus text-teal-600 mr-2"></i> Nouvelle Consultation
            </h2>

            <c:if test="${not empty errorMessage}">
                <div class="mb-4 rounded-lg border border-rose-200 bg-rose-50 p-3 text-sm text-rose-700">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <c:if test="${param.success == 'closed'}">
                <div class="mb-4 rounded-lg border border-emerald-200 bg-emerald-50 p-3 text-sm text-emerald-700">
                    La consultation a été clôturée avec succès.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/doctor/consultations" method="post" class="space-y-4">
                <input type="hidden" name="_csrf" value="${csrfToken}">

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Sélectionner Patient</label>
                    <select name="consultationId" required class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500 focus:outline-none text-sm">
                        <option value="">-- Choisir un patient --</option>
                        <c:forEach var="item" items="${pendingConsultations}">
                            <option value="${item.consultationId}">${item.patient.lastName} ${item.patient.firstName} (N°: ${item.patient.socialSecurityNumber})</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Motif de consultation</label>
                    <input type="text" name="reason" required class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500 focus:outline-none text-sm"/>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Observations</label>
                    <textarea name="observations" required rows="2" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500 focus:outline-none text-sm"></textarea>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Diagnostic</label>
                    <textarea name="diagnosis" required rows="2" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500 focus:outline-none text-sm"></textarea>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Traitement Prescrit</label>
                    <textarea name="prescribedTreatment" required rows="2" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500 focus:outline-none text-sm"></textarea>
                </div>

                <!-- Tarif Fixe -->
                <div class="bg-slate-50 p-3 rounded-lg border border-slate-200 flex justify-between items-center">
                    <span class="text-xs font-bold text-slate-600">Tarif Fixe Consultation</span>
                    <span class="text-sm font-extrabold text-teal-700">150.00 DH</span>
                </div>

                <button type="submit" class="w-full bg-teal-600 hover:bg-teal-700 text-white font-semibold py-2.5 rounded-lg shadow transition duration-200 text-sm">
                    Valider la Consultation
                </button>
            </form>
        </section>

        <!-- Right Column: Liste des Patients en attente -->
        <section class="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-slate-200">
            <h2 class="text-lg font-bold text-slate-800 border-b pb-3 mb-4 flex items-center">
                <i class="bi bi-card-checklist text-teal-600 mr-2"></i> Patients en Attente & Signes Vitaux
            </h2>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <c:forEach var="item" items="${pendingConsultations}">
                    <div class="border rounded-xl p-4 bg-slate-50 hover:border-teal-400 transition shadow-sm">
                        <div class="flex justify-between items-start mb-2">
                            <h3 class="font-bold text-slate-800 text-base">${item.patient.firstName} ${item.patient.lastName}</h3>
                            <span class="text-xs bg-teal-100 text-teal-800 font-semibold px-2 py-0.5 rounded">
                                Consultation #${item.consultationId}
                            </span>
                        </div>
                        <p class="text-xs text-slate-500 mb-3">Né(e) le: ${item.patient.birthDate} — SS: ${item.patient.socialSecurityNumber}</p>

                        <div class="grid grid-cols-2 gap-2 text-xs bg-white p-2.5 rounded-lg border border-slate-200">
                            <div><span class="text-slate-400">Tension:</span> <strong>${item.patient.bloodPressure}</strong></div>
                            <div><span class="text-slate-400">Pouls:</span> <strong>${item.patient.heartRate} BPM</strong></div>
                            <div><span class="text-slate-400">Temp.:</span> <strong>${item.patient.temperature} °C</strong></div>
                            <div><span class="text-slate-400">Resp.:</span> <strong>${item.patient.respiratoryRate} /min</strong></div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty pendingConsultations}">
                    <div class="col-span-2 text-center py-12 text-slate-400 italic">
                        Aucun patient disponible dans la file d'attente.
                    </div>
                </c:if>
            </div>
        </section>

    </main>

</body>
</html>
