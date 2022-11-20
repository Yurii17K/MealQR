        function send() {

        var name = document.getElementById("allergies");
        var result =  document.getElementById("result");

          const json = {
            allergies: allergies,
          };

          fetch("/api/users/update-allergies", {
            method: "PATCH",
            headers:
            {
               "Content-Type": "application/json",
            },
            body: JSON.stringify(json),
          });
       }