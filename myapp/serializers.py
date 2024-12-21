from rest_framework import serializers
from myapp.models import Group, UserToGroup, DirectionOfStudy, User, Teacher, Department, TeacherStatistics


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'name', 'email', 'login', 'surname', 'middle_name', 'birthday', 'sex', 'role']


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'name', 'email', 'login', 'password', 'surname', 'middle_name', 'birthday', 'sex', 'role']

class TeacherSerializer(serializers.ModelSerializer):
    user = UserSerializer()
    department = serializers.PrimaryKeyRelatedField(queryset=Department.objects.all())

    class Meta:
        model = Teacher
        fields = ['id', 'user', 'department', 'year_of_start_of_work']

    def create(self, validated_data):
        user_data = validated_data.pop('user')
        user = User.objects.create(**user_data)
        teacher = Teacher.objects.create(user=user, **validated_data)
        return teacher

    def update(self, instance, validated_data):
        user_data = validated_data.pop('user')
        user = instance.user

        # Обновляем данные пользователя
        user.name = user_data.get('name', user.name)
        user.email = user_data.get('email', user.email)
        user.login = user_data.get('login', user.login)
        user.password = user_data.get('password', user.password)
        user.surname = user_data.get('surname', user.surname)
        user.middle_name = user_data.get('middle_name', user.middle_name)
        user.birthday = user_data.get('birthday', user.birthday)
        user.sex = user_data.get('sex', user.sex)
        user.role = user_data.get('role', user.role)
        user.save()

        # Обновляем данные преподавателя
        instance.department = validated_data.get('department', instance.department)
        instance.year_of_start_of_work = validated_data.get('year_of_start_of_work', instance.year_of_start_of_work)
        instance.save()

        return instance

class TeacherStatisticsSerializer(serializers.ModelSerializer):
    class Meta:
        model = TeacherStatistics
        fields = '__all__'

class DepartmentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Department
        fields = ['id', 'title', 'head_of_department']


class DirectionOfStudySerializer(serializers.ModelSerializer):
    department = serializers.PrimaryKeyRelatedField(queryset=Department.objects.all())

    class Meta:
        model = DirectionOfStudy
        fields = ['id', 'title', 'department', 'code', 'degree']


class GroupSerializer(serializers.ModelSerializer):
    direction = serializers.PrimaryKeyRelatedField(queryset=DirectionOfStudy.objects.all())
    curator = serializers.PrimaryKeyRelatedField(queryset=Teacher.objects.all())

    class Meta:
        model = Group
        fields = ['id', 'direction', 'headmen', 'curator', 'course', 'number']


class UserToGroupSerializer(serializers.ModelSerializer):
    user = UserSerializer()
    group = serializers.PrimaryKeyRelatedField(queryset=Group.objects.all())

    class Meta:
        model = UserToGroup
        fields = ['id', 'user', 'group']


    def create(self, validated_data):
        user_data = validated_data.pop('user')
        user = User.objects.create(**user_data)
        user_to_group = UserToGroup.objects.create(user=user, **validated_data)
        return user_to_group

    def update(self, instance, validated_data):
        user_data = validated_data.pop('user')
        user = instance.user

        # Обновляем данные
        user.name = user_data.get('name', user.name)
        user.email = user_data.get('email', user.email)
        user.login = user_data.get('login', user.login)
        user.password = user_data.get('password', user.password)
        user.surname = user_data.get('surname', user.surname)
        user.middle_name = user_data.get('middle_name', user.middle_name)
        user.birthday = user_data.get('birthday', user.birthday)
        user.sex = user_data.get('sex', user.sex)
        user.role = user_data.get('role', user.role)
        user.save()

        instance.group = validated_data.get('group', instance.group.id)
        instance.save()

        return instance

