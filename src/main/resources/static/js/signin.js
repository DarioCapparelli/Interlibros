
const signin = document.querySelector("#sign-in1");

signin.addEventListener("submit", (e) => {
  e.preventDefault();

  const nombre = document.querySelector("#nombre").value;
  const apellido = document.querySelector("#apellido").value;
  const edad = document.querySelector("#edad").value;
  const email = document.querySelector("#email").value;
  const clave1 = document.querySelector("#clave1").value;
  const clave2 = document.querySelector("#clave2").value;

  console.log(nombre);
  console.log(apellido);
  console.log(edad);
  console.log(email);
  console.log(clave1);
  console.log(clave2);
});