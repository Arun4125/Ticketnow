window.onload = () => {
    const id = new URLSearchParams(window.location.search).get("bookingId");

    if (!id) {
        document.getElementById("details").innerHTML = "<p>No booking ID!</p>";
        return;
    }

    fetch(`http://localhost:8080/api/bookings/${id}`)
        .then(r => r.json())
        .then(b => {

            localStorage.setItem("bookingId", b.id);

            
            const formatDate = (dateStr) => {
                const d = new Date(dateStr);
                const day = d.getDate().toString().padStart(2, "0");
                const month = (d.getMonth() + 1).toString().padStart(2, "0");
                const year = d.getFullYear();

                let hours = d.getHours();
                const minutes = d.getMinutes().toString().padStart(2, "0");
                const ampm = hours >= 12 ? "PM" : "AM";
                hours = hours % 12;
                hours = hours ? hours : 12; 
                const hourStr = hours.toString().padStart(2, "0");

                return `${day}/${month}/${year} ${hourStr}:${minutes} ${ampm}`;
            };

            document.getElementById("details").innerHTML = `
                <p><b>Name:</b> ${b.customerName}</p>
                <p><b>Movie:</b> ${b.movieName}</p>
                <p><b>Theatre:</b> ${b.theatreName}</p>
                <p><b>Show Date & Time:</b> ${formatDate(b.showTime)}</p>
                <p><b>Seats:</b> ${b.seats}</p>
                <p><b>Total Seats:</b> ${b.seatCount}</p>
                <p><b>Booking Time:</b> ${formatDate(b.bookedAt)}</p>
                <p><b>Ticket ID:</b> ${b.id}</p>
                ${b.qrCode ? `<img src="${b.qrCode}" width="140"/>` : ""}
            `;
        });
};

function downloadTicket(){
    const id = localStorage.getItem("bookingId");
    window.location.href = `http://localhost:8080/api/bookings/${id}/pdf`;
}
