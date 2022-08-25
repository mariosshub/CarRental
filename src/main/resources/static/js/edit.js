$('document').ready(function () {
    $('.container #editButton').on('click', function(event){

        event.preventDefault();

        $.get($(this).attr('href'), function (car){
            $('#idEdit').val(car.id);
            $('#modelEdit').val(car.model);
            $('#companyEdit').val(car.company);
            $('#ageEdit').val(car.age);
            $('#ccEdit').val(car.cc);
            $('#priceEdit').val(car.price);
            $('#locationEdit').val(car.location);
            $('#cityEdit').val(car.city);
            $('#availableFromEdit').val(car.availableFrom);
            $('#availableUntilEdit').val(car.availableUntil);

        });
        $('#editModal').modal();
    });
    $('.container #deleteButton').on('click', function(event){
        event.preventDefault();
        let href = $(this).attr('href');
        $('#deleteModal #delRef').attr('href',href);
        $('#deleteModal').modal();
    });
});