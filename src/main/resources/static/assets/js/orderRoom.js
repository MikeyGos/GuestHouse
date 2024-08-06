document.addEventListener('DOMContentLoaded', () => {
    async function fetchBookingNumber() {
        try {

            const response = await fetch('/bnb/login');
            if (!response.ok) {
                throw new Error('Failed to fetch booking number');
            }
            const bookingNumber = await response.text();
            return bookingNumber.trim();
        } catch (error) {
            console.error('Error fetching booking number:', error);
            alert('Error fetching booking number');
            return null;
        }
    }

    async function fetchRooms(bookingNumber) {
        try {

            const response = await fetch(`/bnb/room/${bookingNumber}`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const rooms = await response.json();
            displayRooms(rooms);
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
            alert('Error fetching room orders');
        }
    }

    function displayRooms(rooms) {
        const roomList = document.getElementById('roomTableBody');
        roomList.innerHTML = '';


        rooms.sort((a, b) => {
            if (a.date < b.date) return -1;
            if (a.date > b.date) return 1;
            if (a.time < b.time) return -1;
            if (a.time > b.time) return 1;
            return 0;
        });

        rooms.forEach(room => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${room.date}</td> 
                <td>${room.time}</td>
                <td>${room.roomName}</td>
                <td>
                    <button class="btn btn-primary remove-btn" 
                            data-id="${room.idParty}">
                        Remove
                    </button>
                </td>
            `;
            roomList.appendChild(row);
        });
    }

    async function removeRoom(idParty) {
        try {
            const response = await fetch(`/bnb/room/${idParty}`, {
                method: 'DELETE',
            });
            if (response.ok) {
                alert('Room order removed successfully');

                const bookingNumber = await fetchBookingNumber();
                if (bookingNumber) {
                    fetchRooms(bookingNumber);
                }
            } else {
                alert('Failed to remove room order');
            }
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
            alert('Error removing room order');
        }
    }

    document.addEventListener('click', async (event) => {
        if (event.target && event.target.classList.contains('remove-btn')) {
            const idParty = event.target.getAttribute('data-id');
            await removeRoom(idParty);
        }
    });

    fetchBookingNumber().then(bookingNumber => {
        if (bookingNumber) {
            fetchRooms(bookingNumber);
        }
    });
});