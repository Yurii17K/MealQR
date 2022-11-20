        function send() {
        var name = document.getElementById("name").value;
        var lastName = document.getElementById("lastName").value;
        var city = document.getElementById("city").value;
        var email = document.getElementById("email").value;
        var pass = document.getElementById("pass").value;
        var result = document.getElementById("result");

          const json = {
            name: name,
            lastName: lastName,
            city: city,
            email: email,
            pass: pass,
            client: true,
          };

          fetch("/api/users/sign-up", {
            method: "POST",
            headers:
            {
               "Content-Type": "application/json",
            },
            body: JSON.stringify(json),
          });
       }