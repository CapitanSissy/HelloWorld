$(document).ready(function () {
  var wrapperUrl = "https://api.capitansissy.cw:444/api.php";

  $("#addUserInput").click(function () {
    var newValue = $("input[name='userInput']").val();
    if (newValue === "") {
      // TODO: change css or show message, empty value not valid
    } else {
      $.post(wrapperUrl, {GetInput: {"arg0": newValue}}
      ).done(function (data) {
          if (data === "Could not connect to host") {
            alert("اتصال به سرور میسر نیست");
          }
          // TODO: better error handling
          location.reload(true);
        }
      ).fail(function (data) {
        console.debug(data);
        // TODO: show server's error message.
      });
    }
  });

});