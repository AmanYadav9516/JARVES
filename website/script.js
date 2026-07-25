// ==========================================================================
// JARVES AI - Interactive Website Scripts & Particle Physics
// ==========================================================================

document.addEventListener("DOMContentLoaded", () => {
    initParticleCanvas();
    initCommandPlayground();
    initContactForm();
    initAiSphereInteraction();
});

// Particle Canvas Animation
function initParticleCanvas() {
    const canvas = document.getElementById("particleCanvas");
    if (!canvas) return;

    const ctx = canvas.getContext("2d");
    let width = (canvas.width = window.innerWidth);
    let height = (canvas.height = window.innerHeight);

    window.addEventListener("resize", () => {
        width = canvas.width = window.innerWidth;
        height = canvas.height = window.innerHeight;
    });

    const particles = [];
    const particleCount = 45;

    for (let i = 0; i < particleCount; i++) {
        particles.push({
            x: Math.random() * width,
            y: Math.random() * height,
            vx: (Math.random() - 0.5) * 0.8,
            vy: (Math.random() - 0.5) * 0.8,
            radius: Math.random() * 2 + 1,
            color: Math.random() > 0.5 ? "rgba(0, 242, 254, " : "rgba(127, 0, 255, ",
            alpha: Math.random() * 0.5 + 0.2
        });
    }

    function animate() {
        ctx.clearRect(0, 0, width, height);

        for (let i = 0; i < particleCount; i++) {
            let p = particles[i];
            p.x += p.vx;
            p.y += p.vy;

            if (p.x < 0 || p.x > width) p.vx *= -1;
            if (p.y < 0 || p.y > height) p.vy *= -1;

            ctx.beginPath();
            ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2);
            ctx.fillStyle = p.color + p.alpha + ")";
            ctx.fill();

            // Connect nearby particles
            for (let j = i + 1; j < particleCount; j++) {
                let p2 = particles[j];
                let dist = Math.hypot(p.x - p2.x, p.y - p2.y);
                if (dist < 120) {
                    ctx.beginPath();
                    ctx.moveTo(p.x, p.y);
                    ctx.lineTo(p2.x, p2.y);
                    ctx.strokeStyle = `rgba(0, 242, 254, ${0.15 * (1 - dist / 120)})`;
                    ctx.lineWidth = 0.5;
                    ctx.stroke();
                }
            }
        }

        requestAnimationFrame(animate);
    }

    animate();
}

// Command Playground Switcher
function initCommandPlayground() {
    const chips = document.querySelectorAll(".cmd-chip");
    const hudOutput = document.getElementById("hudOutput");

    const presets = {
        "भाई, मम्मी को कॉल लगा और उसके बाद कैमरा खोल देना": [
            { num: "Step 1", text: "Resolving contact 'Mummy' & triggering ACTION_CALL Intent..." },
            { num: "Step 2", text: "Launching CameraX Engine in Still Photography Mode..." }
        ],
        "YouTube पर Arijit Singh के गाने चलाओ": [
            { num: "Step 1", text: "Searching YouTube API / Deep Link for 'Arijit Singh songs'..." },
            { num: "Step 2", text: "Opening YouTube app & initiating playback..." }
        ],
        "send sms 'HII' to mummy after 30 minutes": [
            { num: "Step 1", text: "Parsing WorkManager delayed task for 30 minutes..." },
            { num: "Step 2", text: "Enqueuing SmsWorker with message payload 'HII' to Mummy..." }
        ],
        "on the flashlight & tell me battery status": [
            { num: "Step 1", text: "Executing CameraManager.setTorchMode(true)..." },
            { num: "Step 2", text: "Reading BatteryManager stats: 84% (Not Charging). Speaking via TTS..." }
        ],
        "Alarm 6 बजे का लगा दो": [
            { num: "Step 1", text: "Triggering ACTION_SET_ALARM for 06:00 AM..." },
            { num: "Step 2", text: "Alarm confirmed and saved to Android Alarm Clock..." }
        ]
    };

    chips.forEach(chip => {
        chip.addEventListener("click", () => {
            chips.forEach(c => c.classList.remove("active"));
            chip.classList.add("active");

            const cmd = chip.getAttribute("data-cmd");
            const steps = presets[cmd] || [
                { num: "Step 1", text: "Processing voice input via NLU pipeline..." }
            ];

            hudOutput.innerHTML = `
                <p class="log-line sys"><span class="prompt">[SYSTEM]</span> JARVES AI Pipeline Initialized...</p>
                <p class="log-line user"><span class="prompt">[VOICE INPUT]</span> "${cmd}"</p>
                <div class="pipeline-steps">
                    ${steps.map(s => `
                        <div class="step-card active">
                            <i class="fa-solid fa-check-circle step-icon"></i>
                            <div class="step-details">
                                <span class="step-num">${s.num}</span>
                                <span class="step-text">${s.text}</span>
                            </div>
                        </div>
                    `).join('')}
                </div>
            `;
        });
    });
}

// Contact Form Handler
function initContactForm() {
    const form = document.getElementById("contactForm");
    const toast = document.getElementById("formToast");

    if (!form) return;

    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const name = document.getElementById("userName").value.trim();
        const email = document.getElementById("userEmail").value.trim();
        const mobile = document.getElementById("userMobile").value.trim();
        const message = document.getElementById("userMessage").value.trim();

        if (name && email && mobile && message) {
            // Show Toast Notification
            toast.classList.remove("hidden");
            form.reset();

            setTimeout(() => {
                toast.classList.add("hidden");
            }, 6000);
        }
    });
}

// Interactive AI Core Hover Reaction
function initAiSphereInteraction() {
    const core = document.getElementById("aiCore");
    if (!core) return;

    core.addEventListener("click", () => {
        core.style.transform = "scale(1.15)";
        setTimeout(() => {
            core.style.transform = "scale(1.0)";
        }, 300);
    });
}
