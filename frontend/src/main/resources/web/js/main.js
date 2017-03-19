+ function($) {
    'use strict';

    // UPLOAD CLASS DEFINITION
    // ======================

    var dropZone = document.getElementById('drop-zone');
    var uploadForm = document.getElementById('js-upload-form');


    uploadForm.addEventListener('submit', function(e) {
        var uploadFiles = document.getElementById('js-upload-files').files;
        e.preventDefault()
        startUpload(uploadFiles)
    })

    dropZone.ondrop = function(e) {
        e.preventDefault();
        this.className = 'upload-drop-zone';
        startUpload(e.dataTransfer.files)
    }

    dropZone.ondragover = function() {
        this.className = 'upload-drop-zone drop';
        return false;
    }

    dropZone.ondragleave = function() {
        this.className = 'upload-drop-zone';
        return false;
    }


    function startUpload(files) {
        console.log(files)

        event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

        // START A LOADING SPINNER HERE

        // Create a formdata object and add the files
        var data = new FormData();

        $.each(files, function(key, value)
          {
             data.append("file", value);
          });

          $.ajax({
             url: 'upload',
             type: 'POST',
             data: data,
             cache: false,
             dataType: 'json',
             processData: false, // Don't process the files
             contentType: false, // Set content type to false as jQuery will tell the server its a query string request
             success: function(data, textStatus, jqXHR)
               {
                 if(typeof data.error === 'undefined')
                   {
                   // Success so call function to process the form
                     updateView(data);
                   }
                 else
                   {
                   // Handle errors here
                     console.log('ERRORS: ' + data);
                   }
               },
                  error: function(jqXHR, textStatus, errorThrown)
                    {
                    // Handle errors here
                     console.log('ERRORS: ' + textStatus);
                    // STOP LOADING SPINNER
                    }
          });
     }



    function updateView(data) {

      var fileName = data.name;
      var wordCount = data.wordCount;
      $(".list-group")
        .empty()
        .append("<a href='#' class='list-group-item list-group-item-success'><span class='badge alert-success pull-right'>Number of Words: " + wordCount + "</span>" + fileName + "</a>")

    }



}(jQuery);