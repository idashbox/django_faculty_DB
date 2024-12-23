from django.db.models import Q
from django.shortcuts import render
from rest_framework import viewsets, generics
from myapp.models import User, Teacher, Department, UserToGroup, Group, DirectionOfStudy, TeacherStatistics
from myapp.serializers import UserSerializer, TeacherSerializer, DepartmentSerializer, UserToGroupSerializer, \
    GroupSerializer, DirectionOfStudySerializer, TeacherStatisticsSerializer
from rest_framework import filters


def home(request):
    return render(request, 'home.html')


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class TeacherViewSet(viewsets.ModelViewSet):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer

    def get_queryset(self):
        queryset = self.queryset
        name = self.request.query_params.get('name')
        surname = self.request.query_params.get('surname')
        middle_name = self.request.query_params.get('middle_name')
        birthday = self.request.query_params.get('birthday')
        department = self.request.query_params.get('department')
        order_by = self.request.query_params.get('orderBy')

        if name:
            queryset = queryset.filter(Q(user__name__icontains=name))
        if surname:
            queryset = queryset.filter(Q(user__surname__icontains=surname))
        if middle_name:
            queryset = queryset.filter(Q(user__middle_name__icontains=middle_name))
        if birthday:
            queryset = queryset.filter(Q(user__birthday__icontains=birthday))
        if department:
            queryset = queryset.filter(Q(department_id=department))
        if order_by:

            valid_order_fields = ['id', 'user__name', 'user__surname', 'user__middle_name', 'user__birthday']
            if order_by in valid_order_fields:
                queryset = queryset.order_by(order_by)
            else:
                raise ValueError(f"Invalid order_by field: {order_by}")

        return queryset


class TeacherSearchView(generics.ListAPIView):
    serializer_class = TeacherSerializer

    def get_queryset(self):
        query = self.request.query_params.get('query', '')
        if query:
            return Teacher.objects.filter(
                Q(user__name__icontains=query) |
                Q(user__surname__icontains=query) |
                Q(user__middle_name__icontains=query) |
                Q(user__birthday__icontains=query)
            )
        return Teacher.objects.none()


class StatisticsView(viewsets.ReadOnlyModelViewSet):
    queryset = TeacherStatistics.objects.all()
    serializer_class = TeacherStatisticsSerializer


class DepartmentViewSet(viewsets.ModelViewSet):
    queryset = Department.objects.all()
    serializer_class = DepartmentSerializer


class UserDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class DepartmentDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Department.objects.all()
    serializer_class = DepartmentSerializer


class TeacherDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer


class DirectionOfStudyViewSet(viewsets.ModelViewSet):
    queryset = DirectionOfStudy.objects.all()
    serializer_class = DirectionOfStudySerializer


class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


class UserToGroupViewSet(viewsets.ModelViewSet):
    queryset = UserToGroup.objects.all()
    serializer_class = UserToGroupSerializer

    def get_queryset(self):
        queryset = self.queryset
        name = self.request.query_params.get('name')
        surname = self.request.query_params.get('surname')
        middle_name = self.request.query_params.get('middle_name')
        birthday = self.request.query_params.get('birthday')
        department = self.request.query_params.get('department')
        order_by = self.request.query_params.get('orderBy')

        if name:
            queryset = queryset.filter(Q(user__name__icontains=name))
        if surname:
            queryset = queryset.filter(Q(user__surname__icontains=surname))
        if middle_name:
            queryset = queryset.filter(Q(user__middle_name__icontains=middle_name))
        if birthday:
            queryset = queryset.filter(Q(user__birthday__icontains=birthday))
        if department:
            queryset = queryset.filter(Q(department_id=department))
        if order_by:

            valid_order_fields = ['id', 'user__name', 'user__surname', 'user__middle_name', 'user__birthday']
            if order_by in valid_order_fields:
                queryset = queryset.order_by(order_by)
            else:
                raise ValueError(f"Invalid order_by field: {order_by}")

        return queryset

class DirectionOfStudyView(generics.RetrieveUpdateDestroyAPIView):
    queryset = DirectionOfStudy.objects.all()
    serializer_class = DirectionOfStudySerializer


class GroupView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


class UserToGroupView(generics.RetrieveUpdateDestroyAPIView):
    queryset = UserToGroup.objects.all()
    serializer_class = UserToGroupSerializer
