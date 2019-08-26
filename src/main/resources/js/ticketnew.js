var createticket = {};

createticket.module = (function () {

    var createIssue = function() {


//
//         Ajax/Jquery
//
//         $(".uploadDocumentGeneral").on("click", function (evt) {
//
//             var documentData = new FormData();
//             documentData.append('file', $('input#file.findDocumentOnboarding')[0].files[0]);
//             $.ajax({
//                 url: url,
//                 type: 'POST',
//                 data: documentData,
//                 cache: false,
//                 contentType: false,
//                 processData: false,
//                 success: function (response) {
//                     alert("Document uploaded successfully.");
//                 }
//             });
//
//             return false;
//         });
//
//         JAVA
//
// @POST
// @Path("upload/{id}")
// @Consumes({"application/x-www-form-urlencoded", "multipart/form-data"})
//
//         public void addBlob(@PathParam("id") Integer id, @FormDataParam("file") InputStream uploadedInputStream) throws IOException {
//             E24ClientTemp entityToMerge = find(id);
//             try {
//                 ByteArrayOutputStream out = new ByteArrayOutputStream();
//                 int read = 0;
//                 byte[] bytes = new byte[1024];
//                 while ((read = uploadedInputStream.read(bytes)) != -1) {
//                     out.write(bytes, 0, read);
//                 }
//                 entityToMerge.setTestBlob(out.toByteArray());
//                 super.edit(entityToMerge);
//             }
//             catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }




//         var formData = new FormData();
//         formData.append('logo', logoImg);
//         var objArr = [];
//
//         objArr.push({"id": id, "name": userName});
//
// //JSON obj
//         formData.append('objArr', JSON.stringify( objArr ))l
//
//
//         $.ajax({
//             url: url,
//             type:"POST",
//             processData:false,
//             contentType: false,
//             data: formData,
//             complete: function(data){
//                 alert("success");
//             }
//         });
//         Async is a bad idea that is deprecated by many browsers, and cache is not needed for POSTs.
//
//                                                                                                  JΛ̊KE



        var restUrl = AJS.params.baseURL + "/rest/exploretickets/1.0/service/createticket";
        var jsonData = {
            "fields": {
                "project":
                    {
                        "key": "ZVK",
                    },
                "summary": AJS.$("#ticket-theme").val(),
                "description": AJS.$("#ticket-input").val(),
                "issuetype": {
                    "id": "10000",
                }
            }
        };

        // var formDataTicket = new FormData();
        // formDataTicket.append("ticket-theme", AJS.$("#ticket-theme").val());
        // formDataTicket.append("ticket-text", AJS.$("#ticket-text").val());
        // formDataTicket.append("ticket-file-upload", AJS.$("#ticket-file-upload")[0].files);

        var form = AJS.$("#ticket-form")[0];
        // Create an FormData object
        var formDataTicket = new FormData(form);


        AJS.$.ajax({
            url: restUrl,
            type: 'post',
            enctype: 'multipart/form-data',
            processData: false,  // Important!
            dataType: 'json',
            data: formDataTicket,
            cache: false,
            async: true,
            // async: asyncFlag,
            // contentType: "application/json; charset=utf-8",
            contentType: false,
            success: function (data) {

            },
            error: function (data) {

            },
            complete: function () {

            },

        });
    };

    return {
        createIssue:createIssue
    };

}());
