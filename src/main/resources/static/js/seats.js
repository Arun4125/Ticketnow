const urlParams = new URLSearchParams(window.location.search);
const screeningId = urlParams.get("screeningId");

let selected = [];
let otpVerified = false;

const grid = document.getElementById("seatGrid");
const confirmBtn = document.getElementById("confirmBtn");
const nameForm = document.getElementById("nameForm");

const submitBtn = document.getElementById("submitBooking");
const cancelBtn = document.getElementById("cancelBooking");
const sendOtpBtn = document.getElementById("sendOtpBtn");
const verifyOtpBtn = document.getElementById("verifyOtpBtn");

const customerInput = document.getElementById("customerName");
const phoneInput = document.getElementById("phone");
const otpInput = document.getElementById("otp");
phoneInput.addEventListener("input", () => {
    const phone = phoneInput.value.trim();
    if (/^\d{10}$/.test(phone)) {
        // hide the warning immediately when input is valid
        warningMessage.classList.add("hidden");
    }
});

const warningMessage = document.getElementById("warningMessage");
const successTick = document.getElementById("successTick");
const selectedSeatsSpan = document.getElementById("selectedSeats");
const otpMessage = document.getElementById("otpMessage");

/* ---------------- SEAT LOAD ---------------- */

fetch(`http://localhost:8080/api/seats/${screeningId}`)
.then(res => res.json())
.then(seats => {

    const seatMap = {};
    seats.forEach(s => {
        if (!seatMap[s.seatRow]) seatMap[s.seatRow] = [];
        seatMap[s.seatRow].push(s);
    });

    const rows = Object.keys(seatMap).sort();
    const cols = seatMap[rows[0]].map(s => s.seatCol).sort((a,b)=>a-b);

    grid.style.gridTemplateColumns = `40px repeat(${cols.length}, 32px)`;
    grid.innerHTML = "";

    grid.appendChild(document.createElement("div"));

    cols.forEach(col => {
        const d = document.createElement("div");
        d.className = "col-label";
        d.innerText = col;
        grid.appendChild(d);
    });

    rows.forEach(row => {
        const r = document.createElement("div");
        r.className = "row-label";
        r.innerText = row;
        grid.appendChild(r);

        seatMap[row]
            .sort((a,b)=>a.seatCol-b.seatCol)
            .forEach(s => {
                const seat = document.createElement("div");
                seat.className = "seat " + (s.booked ? "booked" : "free");
                if (!s.booked) {
                    seat.onclick = () => toggleSeat(seat, `${row}${s.seatCol}`);
                }
                grid.appendChild(seat);
            });
    });
});

/* ---------------- SEAT SELECT ---------------- */

function toggleSeat(div, code) {
    if (selected.includes(code)) {
        selected = selected.filter(s => s !== code);
        div.classList.remove("selected");
    } else {
        selected.push(code);
        div.classList.add("selected");
    }
    selectedSeatsSpan.innerText = selected.join(", ");
}

/* ---------------- CONFIRM ---------------- */

confirmBtn.onclick = () => {
    if (selected.length === 0) {
        showWarn("Please select at least one seat!");
        return;
    }
    warningMessage.classList.add("hidden");
    nameForm.classList.remove("hidden");
};

/* ---------------- SEND OTP ---------------- */
sendOtpBtn.onclick = () => {
    const phone = phoneInput.value.trim();
    if (!/^\d{10}$/.test(phone)) {
        showWarn("Enter a valid 10-digit phone number!");
        return;
    }

    fetch("http://localhost:8080/api/otp/send", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phone })
    })
    .then(res => {
        if (!res.ok) throw new Error("Network error");

        
        return res.text().then(txt => {
            try { return JSON.parse(txt); } catch { return {}; }
        });
    })
    .then(data => {
        console.log("Dev OTP:", data.otp || "No OTP returned");
        showOtpMessage("OTP sent successfully!");
        otpInput.classList.remove("hidden");
        verifyOtpBtn.classList.remove("hidden");
    })
    .catch(err => {
        console.error("Fetch error:", err);
        showWarn("OTP send failed");
    });
};



/* ---------------- VERIFY OTP ---------------- */

verifyOtpBtn.onclick = () => {
    otpMessage.classList.remove("show");
    otpMessage.classList.add("hidden");
    
    if (!otpInput.value.trim()) {
        showWarn("Enter OTP!");
        return;
    }
    
    fetch("http://localhost:8080/api/otp/verify", {

        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({
            phone: phoneInput.value,
            otp: otpInput.value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        otpVerified = true;
        showOtpMessage("OTP verified successfully!");
        submitBtn.classList.remove("hidden");
    })
    .catch(() => showWarn("Invalid OTP!"));
};

/* ---------------- SUBMIT BOOKING ---------------- */

submitBtn.onclick = () => {
    if (!otpVerified) {
        showWarn("Please verify OTP first!");
        return;
    }

    const name = customerInput.value.trim();
    if (!name) {
        showWarn("Enter your name!");
        return;
    }

    fetch("http://localhost:8080/api/bookings", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({
            customerName: name,
            screeningId: screeningId,
            seats: selected
        })
    })
    .then(r => r.json())
    .then(res => {
        successTick.classList.remove("hidden");
        setTimeout(() => {
            window.location.href = `summary.html?bookingId=${res.id}`;
        }, 1500);
    })
    .catch(() => showWarn("Booking failed"));
};

/* ---------------- CANCEL ---------------- */

cancelBtn.onclick = () => {
    nameForm.classList.add("hidden");
};

/* ---------------- HELPER ---------------- */

function showWarn(msg) {
    warningMessage.innerText = msg;
    warningMessage.classList.remove("hidden");
    
}
function showOtpMessage(msg, success = true, duration = 3000) {
    const otpMsg = document.getElementById("otpMessage");
    otpMsg.innerText = msg;
    otpMsg.style.backgroundColor = success ? "#27ae60" : "#e74c3c"; // green/red
    otpMsg.classList.add("show");
    otpMsg.classList.remove("hidden");

    setTimeout(() => {
        otpMsg.classList.remove("show");
        otpMsg.classList.add("hidden");
    }, duration);
}