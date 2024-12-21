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
    queryset = Teacher.objects.order_by('id')
    serializer_class = TeacherSerializer
    filter_backends = [filters.SearchFilter]
    search_fields = ['user__first_name', 'user__surname', 'user__middle_name']


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

class DirectionOfStudyView(generics.RetrieveUpdateDestroyAPIView):
    queryset = DirectionOfStudy.objects.all()
    serializer_class = DirectionOfStudySerializer


class GroupView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


class UserToGroupView(generics.RetrieveUpdateDestroyAPIView):
    queryset = UserToGroup.objects.all()
    serializer_class = UserToGroupSerializer
