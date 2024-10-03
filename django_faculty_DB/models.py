from django.db import models


class User(models.Model):
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=150, unique=True)
    email = models.EmailField(unique=True)
    login = models.CharField(max_length=150)
    password = models.CharField(max_length=128)
    surname = models.CharField(max_length=30, blank=True)
    middle_name = models.CharField(max_length=30, blank=True)
    birthday = models.DateField(blank=True)
    sex = models.CharField(max_length=30, blank=True)
    role = models.CharField(max_length=30, blank=True)

    def __str__(self):
        return self.name


class Department(models.Model):
    id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=100)
    head_of_department = models.OneToOneField('Teacher', on_delete=models.CASCADE, related_name='head_of_department')

    def __str__(self):
        return self.title


class DirectionOfStudy(models.Model):
    id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=100)
    department = models.ForeignKey('Department', related_name='directions', on_delete=models.CASCADE)
    code = models.CharField(max_length=100)
    degree = models.CharField(max_length=100)

    def __str__(self):
        return self.title


class Group(models.Model):
    id = models.AutoField(primary_key=True)
    direction = models.ForeignKey('DirectionOfStudy', related_name='groups', on_delete=models.CASCADE)
    headmen = models.OneToOneField('UserToGroup', on_delete=models.CASCADE, related_name='headman')
    curator = models.OneToOneField('Teacher', on_delete=models.CASCADE, related_name='curator')
    course = models.CharField(max_length=100)
    number = models.CharField(max_length=100)

    def __str__(self):
        return f"{self.direction.title} - {self.course} - {self.number}"


class Teacher(models.Model):
    id = models.AutoField(primary_key=True)
    user = models.OneToOneField('User', on_delete=models.CASCADE)
    department = models.ForeignKey('Department', related_name='teachers', on_delete=models.CASCADE, null=True, blank=True)
    year_of_start_of_work = models.CharField(max_length=100)

    def __str__(self):
        return f"{self.user.name} -{self.user.middle_name} - {self.user.surname}"


class Discipline(models.Model):
    id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=100)
    lecture_teacher = models.ForeignKey('Teacher', related_name='lecture_disciplines', on_delete=models.CASCADE)
    practice_teacher = models.ForeignKey('Teacher', related_name='practice_disciplines', on_delete=models.CASCADE)
    direction = models.ForeignKey('DirectionOfStudy', related_name='disciplines', on_delete=models.CASCADE)

    def __str__(self):
        return self.title


class UserToGroup(models.Model):
    id = models.AutoField(primary_key=True)
    user = models.ForeignKey('User', on_delete=models.CASCADE)
    group = models.ForeignKey('Group', on_delete=models.CASCADE)

    def __str__(self):
        return f"{self.user.name} - {self.group.course} - {self.group.number}"

#python manage.py flush
#python manage.py create_test_data
#python manage.py makemigrations
#python manage.py migrate



