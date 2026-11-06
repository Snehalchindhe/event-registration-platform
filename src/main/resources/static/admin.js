
const API_BASE = "/api";

const attendeeTable = document.getElementById("attendeeTable");
const searchInput = document.getElementById("searchInput");
const refreshBtn = document.getElementById("refreshBtn");

let allRegistrations = [];

async function loadDashboard() {
    const message = document.getElementById("dashboardMessage");

    try {
        message.textContent = "";

        const [registrationResponse, eventResponse] = await Promise.all([
            fetch(`${API_BASE}/admin/registrations`),
            fetch(`${API_BASE}/events`)
        ]);

        if (!registrationResponse.ok || !eventResponse.ok) {
            throw new Error("Unable to load dashboard data.");
        }

        allRegistrations = await registrationResponse.json();
        const events = await eventResponse.json();

        document.getElementById("totalRegistrations").textContent =
            allRegistrations.length;

        document.getElementById("confirmedRegistrations").textContent =
            allRegistrations.filter(item =>
                (item.status || "").toLowerCase() === "confirmed"
            ).length;

        document.getElementById("totalEvents").textContent = events.length;

        displayRegistrations(allRegistrations);

    } catch (error) {
        attendeeTable.innerHTML = `
            <tr>
                <td colspan="6" class="table-message">
                    Could not load registrations. Please check your server.
                </td>
            </tr>
        `;

        message.textContent = error.message;
        console.error("Dashboard loading error:", error);
    }
}

function displayRegistrations(registrations) {
    document.getElementById("resultCount").textContent =
        `${registrations.length} participant${registrations.length === 1 ? "" : "s"}`;

    if (!registrations.length) {
        attendeeTable.innerHTML = `
            <tr>
                <td colspan="6" class="table-message">
                    No registrations found.
                </td>
            </tr>
        `;
        return;
    }

    attendeeTable.innerHTML = registrations.map(item => {
        const status = (item.status || "confirmed").toLowerCase();

        let statusClass = "status-other";

        if (status === "confirmed") {
            statusClass = "status-confirmed";
        } else if (status === "pending") {
            statusClass = "status-pending";
        }

        const registeredDate = item.registeredAt
            ? new Date(item.registeredAt).toLocaleDateString("en-IN", {
                day: "2-digit",
                month: "short",
                year: "numeric"
            })
            : "—";

        return `
            <tr>
                <td>
                    <div class="participant-name">${escapeHTML(item.name || "—")}</div>
                    <div class="participant-email">${escapeHTML(item.email || "—")}</div>
                </td>

                <td>${escapeHTML(item.college || "—")}</td>

                <td>
                    <div class="event-name">${escapeHTML(item.eventTitle || "—")}</div>
                </td>

                <td>${escapeHTML(item.domainInterests || "—")}</td>

                <td>${escapeHTML(registeredDate)}</td>

                <td>
                    <span class="status-badge ${statusClass}">
                        ${escapeHTML(status)}
                    </span>
                </td>
            </tr>
        `;
    }).join("");
}

function escapeHTML(value) {
    return String(value).replace(/[&<>"']/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#039;"
    })[character]);
}

// Search participants using the backend API
async function searchRegistrations() {
    const searchValue = searchInput.value.trim();

    if (!searchValue) {
        displayRegistrations(allRegistrations);
        return;
    }

    try {
        const response = await fetch(
            `${API_BASE}/admin/registrations?search=${encodeURIComponent(searchValue)}`
        );

        if (!response.ok) {
            throw new Error("Search failed.");
        }

        const results = await response.json();
        displayRegistrations(results);

    } catch (error) {
        document.getElementById("dashboardMessage").textContent =
            error.message;
    }
}

searchInput.addEventListener("input", searchRegistrations);

refreshBtn.addEventListener("click", () => {
    searchInput.value = "";
    loadDashboard();
});

// Initial dashboard load
loadDashboard();
