         function send() {
          var userEmail = document.getElementById("userEmail").value;
          var userPass = document.getElementById("userPass").value;
          var result =  document.getElementById("result");

          const json = {
            userEmail: userEmail,
            userPass: userPass,
          };

          fetch("/api/users/sign-in", {
            method: "POST",
            headers:
            {
               "Content-Type": "application/json",
            },
            body: JSON.stringify(json),
          });
       }