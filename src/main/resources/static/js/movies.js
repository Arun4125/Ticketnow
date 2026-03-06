fetch("http://localhost:8080/api/movies")
.then(res => res.json())
.then(data => {

    const container = document.getElementById("movies");
    container.innerHTML = "";

    data.forEach(m => {
        const div = document.createElement("div");
        div.className = "movie-card";

        div.innerHTML = `
            <img src="images/posters/${m.poster}">
            <h3>${m.title}</h3>
            <a href="theatre.html?movieId=${m.id}" class="book-btn">Book Ticket</a>
        `;
        container.appendChild(div);
    });
});
