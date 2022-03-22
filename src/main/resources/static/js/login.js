const registro = document.querySelector("#login");

registro.addEventListener("submit", (e) => {
  e.preventDefault();

  const email = document.querySelector("#email").value;
  const clave = document.querySelector("#clave").value;

  console.log(email);
  console.log(clave);
});