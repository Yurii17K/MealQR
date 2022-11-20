         function send() {
          var userEmail = document.getElementById("userEmail").value;
          var result =  document.getElementById("result");

          const json = {
            userEmail: userEmail,
          };

          fetch("/data", {
            method: "POST",
            headers:
            {
               "Content-Type": "application/json",
            },
            body: JSON.stringify(json),
          });
       }