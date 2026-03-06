const movieId = new URLSearchParams(window.location.search).get("movieId");

fetch(`http://localhost:8080/api/movies/${movieId}/theatres`)
.then(res => res.json())
.then(theatres => {
    const list = document.getElementById("theatreList");

    theatres.forEach(t => {
        const div = document.createElement("div");
        div.className = "theatre-card";

       div.innerHTML = `
           <h3>${t.name}</h3>
           <p>${t.location}</p>
       `;

        
        div.onclick = () => toggleScreenings(t.id);

        list.appendChild(div);
    });
});

function toggleScreenings(theatreId) {

    const container =  document.getElementById("screeningsContainer");

    container.innerHTML = `
        <table class="screening-table">
            <thead>
                <tr>
                    <th>Show Time</th>
                    <th>Available Seats</th>
                </tr>
            </thead>
            <tbody id="tbody">
                <tr><td colspan="2">Loading screenings...</td></tr>
            </tbody>
        </table>
    `;

    const tbody = document.getElementById("tbody");

    fetch(`http://localhost:8080/api/screenings?movieId=${movieId}&theatreId=${theatreId}`)
        .then(res => res.json())
       .then(slots => {
    tbody.innerHTML = "";

    if (slots.length === 0) {
        tbody.innerHTML = `<tr><td colspan="2">No screenings available.</td></tr>`;
        return;
    }

   
    slots.sort((a, b) =>
        new Date(a.showTime.replace(" ", "T")) -
        new Date(b.showTime.replace(" ", "T"))
    );

    
    Promise.all(
        slots.map(s =>
            fetch(`http://localhost:8080/api/screenings/available/${s.id}`)
                .then(r => r.text())
                .then(count => ({ ...s, count }))
        )
    ).then(results => {

        results.forEach(s => {
            const showDate = new Date(s.showTime.replace(" ", "T"));

            const row = document.createElement("tr");
            row.innerHTML = `
                <td> 
                    ${showDate.toLocaleDateString('en-GB')}
                    ${showDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </td>
                <td>${s.count}</td>
            `;

            row.onclick = () => {
                window.location.href = `seats.html?screeningId=${s.id}`;
            };

            tbody.appendChild(row);
        });

    });
})

        .catch(() => {
            tbody.innerHTML = `<tr><td colspan="2">Error loading screenings</td></tr>`;
        });
}
