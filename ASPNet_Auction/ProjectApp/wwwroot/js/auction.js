$(document).ready(function () {
    alert("test");
    const getEl = (id) => document.getElementById(id);
    const showToast = (icon, title) => {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon,
            title,
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
        });
    };

    const registrationModal = getEl('registrationModal');
    const loginModal = getEl('loginModal');

    // Show/Hide modal
    const toggleModal = (modal, show) => {
        modal.style.display = show ? 'block' : 'none';
    };

    // Register button click event
    

    // Login button click event
    //$('#loginButton').on('click', function (e) {
    //    e.preventDefault();
    //    toggleModal(loginModal, true);
    //});

    // Close Register Modal button click event
    $('#closeButton').on('click', function () {
        toggleModal(registrationModal, false);
    });

    // Close Login Modal button click event
    $('#closeLoginButton').on('click', function () {
        toggleModal(loginModal, false);
    });

    window.addEventListener('click', (e) => {
        if (e.target === registrationModal) toggleModal(registrationModal, false);
        if (e.target === loginModal) toggleModal(loginModal, false);
    });

    // Login form submission
    //$('#loginForm').on('submit', function (e) {
    //    e.preventDefault();

    //    const email = getEl('loginEmail').value.trim();
    //    const password = getEl('loginPassword').value;

    //    // Basic validation
    //    if (!email || !password) return showToast('error', 'Please enter both email and password');

    //    toggleModal(loginModal, false);

    //    // Add two new buttons to the navbar after login
    //    $('#newButton1').show();
    //    $('#newButton2').show();

    //    console.log('Login data:', { email, password });
    });
});
