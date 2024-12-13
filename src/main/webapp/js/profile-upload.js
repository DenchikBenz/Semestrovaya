document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM загружен!');
    const fileInput = document.getElementById('fileInput');
    const imagePreview = document.getElementById('imagePreview');
    const uploadButton = document.getElementById('uploadButton');

    console.log('Elements:', {fileInput, imagePreview, uploadButton});

    if (!fileInput || !imagePreview || !uploadButton) {
        console.error('Не все элементы найдены на странице!');
        return;
    }

    fileInput.addEventListener('change', function() {
        console.log('Файл выбран');
        const file = this.files[0];
        if (file) {
            console.log('Тип файла:', file.type);
            if (!file.type.startsWith('image/')) {
                alert('Пожалуйста, выберите изображение');
                this.value = '';
                uploadButton.disabled = true;
                return;
            }

            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.src = e.target.result;
                console.log('Предпросмотр обновлен');
            };
            reader.readAsDataURL(file);
            uploadButton.disabled = false;
        }
    });

    uploadButton.addEventListener('click', function() {
        console.log('Кнопка загрузки нажата');
        const file = fileInput.files[0];
        if (!file) {
            console.log('Файл не выбран');
            alert('Пожалуйста, выберите файл');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        
        console.log('Отправляем запрос на:', '/uploadProfilePhoto');
        
        fetch('/uploadProfilePhoto', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            console.log('Получен ответ:', response);
            return response.json();
        })
        .then(data => {
            console.log('Данные:', data);
            if (data.success) {
                alert('Фото успешно загружено!');
                imagePreview.src = data.photoUrl;
            } else {
                alert('Ошибка: ' + data.error);
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Произошла ошибка при загрузке файла');
        });
    });
});