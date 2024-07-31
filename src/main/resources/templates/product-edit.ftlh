<!DOCTYPE html>
<html>
<head>
    <title>Редактирование продукта</title>
</head>
<body>
<h1>Редактирование продукта</h1>
<form action="/product/${product.id}/edit" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${product.id}" />
    <div>
        <label for="title">Название:</label>
        <input type="text" id="title" name="title" value="${product.title}" required />
    </div>
    <div>
        <label for="description">Описание:</label>
        <textarea id="description" name="description" required>${product.description}</textarea>
    </div>
    <div>
        <label for="city">Город:</label>
        <input type="text" id="city" name="city" value="${product.city}" required />
    </div>
    <div>
        <label for="author">Автор:</label>s
        <input type="text" id="author" name="author" value="${product.user.name}" required />
    </div>
    <div>
        <label for="price">Цена:</label>
        <input type="text" id="price" name="price" value="${product.price}" required/>
    </div>
    <div>
        Первая фотография: <input type="file" name="file" ><br><br>
        Вторая фотография: <input type="file" name="file1" ><br><br>
        Третья фотография: <input type="file" name="file2" ><br><br>
        <#list images as img>
            <img src="/images/${img.id}" height="60px"/><br><br>
        </#list>
    </div>
    <div>
        <button type="submit">Сохранить изменения</button>
    </div>
</form>
</body>
</html>