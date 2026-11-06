
const API_BASE = "/api";

const eventGrid = document.getElementById("eventGrid");
const filterButtons = document.querySelectorAll(".filter-chip");
const registrationModal = document.getElementById("registrationModal");
const registrationForm = document.getElementById("registrationForm");

let allEvents = [];
let selectedEventId = null;

const categoryDetails = {
    technology: { symbol: "✳", className: "technology" },
    development: { symbol: "</>", className: "development" },
    security: { symbol: "◈", className: "security" }
};

// Load events from the Spring Boot backend
async function loadEvents() {
    try {
        const response = await fetch(`${API_BASE}/events`);

        if (!response.ok) {
            throw new Error("Unable to load events.");
        }

        allEvents = await response.json();
        displayEvents(allEvents);
    } catch (error) {
        eventGrid.innerHTML = `
            <div class="empty-state">
                <h3>Events could not be loaded</h3>
                <p>Please make sure the server is running and try again.</p>
            </div>
        `;
        console.error("Event loading error:", error);
    }
}

// Display event cards
function displayEvents(events) {
    if (!events.length) {
        eventGrid.innerHTML = `
            <div class="empty-state">
                <h3>No events found</h3>
                <p>Try selecting another category.</p>
            </div>
        `;
        return;
    }

    eventGrid.innerHTML = events.map(event => {
        const category = (event.category || "").toLowerCase();
        const artwork = categoryDetails[category] || {
            symbol: "✦",
            className: "technology"
        };

        const eventDate = new Date(event.eventDate);
        const formattedDate = eventDate.toLocaleString("en-IN", {
            day: "numeric",
            month: "short",
            year: "numeric",
            hour: "numeric",
            minute: "2-digit"
        });

        const availableSeats = Number(event.availableSeats ?? 0);
        const capacity = Number(event.capacity ?? 0);
        const registeredCount = Number(event.registeredCount ?? 0);
        const progress = capacity > 0
            ? Math.min((registeredCount / capacity) * 100, 100)
            : 0;

        return `
            <article class="event-card">
                <div class="event-art ${artwork.className}">
                    <span class="art-symbol">${artwork.symbol}</span>
                    <span class="art-label">${escapeHTML(event.category || "Event")}</span>
                </div>

                <div class="event-card-content">
                    <span class="event-category">${escapeHTML(event.category || "General")}</span>

                    <h3>${escapeHTML(event.title)}</h3>

                    <p class="event-description">
                        ${escapeHTML(event.description || "Join us for this exciting event.")}
                    </p>

                    <div class="event-details">
                        <div>
                            <span class="detail-icon">◷</span>
                            ${escapeHTML(formattedDate)}
                        </div>
                        <div>
                            <span class="detail-icon">⌖</span>
                            ${escapeHTML(event.venue || "Venue to be announced")}
                        </div>
                    </div>

                    <div class="capacity-info">
                        <div class="capacity-text">
                            <span>${registeredCount} registered</span>
                            <span>${availableSeats} seats left</span>
                        </div>
                        <div class="capacity-track">
                            <div class="capacity-fill" style="width: ${progress}%"></div>
                        </div>
                    </div>

                    <button
                        class="register-btn"
                        data-event-id="${event.id}"
                        ${availableSeats <= 0 ? "disabled" : ""}
                    >
                        ${availableSeats <= 0 ? "Fully Booked" : "Register Now"}
                        <span>→</span>
                    </button>
                </div>
            </article>
        `;
    }).join("");

    document.querySelectorAll(".register-btn").forEach(button => {
        button.addEventListener("click", () => {
            openRegistrationModal(Number(button.dataset.eventId));
        });
    });
}

// Prevent HTML injection when displaying event data
function escapeHTML(value) {
    return String(value).replace(/[&<>"']/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#039;"
    })[character]);
}
// Category filters
filterButtons.forEach(button => {
    button.addEventListener("click", () => {
        filterButtons.forEach(item => item.classList.remove("selected"));
        button.classList.add("selected");

        const selectedCategory = button.dataset.category.toLowerCase();

        if (selectedCategory === "all") {
            displayEvents(allEvents);
        } else {
            const filteredEvents = allEvents.filter(event =>
                (event.category || "").trim().toLowerCase() === selectedCategory
            );

            displayEvents(filteredEvents);
        }
    });
});

// Open registration modal
function openRegistrationModal(eventId) {
    const event = allEvents.find(item => Number(item.id) === eventId);

    if (!event) return;

    selectedEventId = eventId;
    registrationForm.reset();

    const message = document.getElementById("formMessage");
    if (message) message.textContent = "";

    const eventTitle = document.getElementById("modalEventTitle");
    if (eventTitle) eventTitle.textContent = event.title;

    const hiddenEventId = document.getElementById("eventId");
    if (hiddenEventId) hiddenEventId.value = eventId;

    registrationModal.classList.add("open");
    document.body.style.overflow = "hidden";
}

// Close registration modal
function closeRegistrationModal() {
    registrationModal.classList.remove("open");
    document.body.style.overflow = "";
    selectedEventId = null;
}

const closeModalButton = document.getElementById("closeModal");

if (closeModalButton) {
    closeModalButton.addEventListener("click", closeRegistrationModal);
}

registrationModal.addEventListener("click", event => {
    if (event.target === registrationModal) {
        closeRegistrationModal();
    }
});
document.addEventListener("keydown", event => {
    if (event.key === "Escape" && registrationModal.classList.contains("open")) {
        closeRegistrationModal();
    }
});

// Submit participant registration
registrationForm.addEventListener("submit", async event => {
    event.preventDefault();

    if (!selectedEventId) return;

    const submitButton = registrationForm.querySelector('[type="submit"]');
    const message = document.getElementById("formMessage");

    const registrationData = {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        college: document.getElementById("college").value.trim(),
        domainInterests: document.getElementById("domainInterests").value.trim()
    };

    if (message) {
        message.textContent = "Submitting your registration...";
        message.className = "form-message";
    }

    submitButton.disabled = true;
    submitButton.textContent = "Submitting...";

    try {
        const response = await fetch(
            `${API_BASE}/events/${selectedEventId}/registrations`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(registrationData)
            }
        );

        const result = await response.json().catch(() => ({}));

        if (!response.ok) {
            throw new Error(
                result.message || result.error || "Registration failed. Please try again."
            );
        }

        if (message) {
            message.textContent = "Registration successful! You are on the attendee list.";
            message.className = "form-message success";
        }

        registrationForm.reset();

        await loadEvents();

        setTimeout(() => {
            closeRegistrationModal();
        }, 1800);

    } catch (error) {
        if (message) {
            message.textContent = error.message;
            message.className = "form-message error";
        }
    } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Complete Registration";
    }
});

// Initial page load
loadEvents();
