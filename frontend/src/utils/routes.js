import allroles from '../utils/allroles';
import HomePage from '../pages/HomePage'
import LoginPage from '../pages/LoginPage';
import SignupPage from '../pages/SignupPage'
import ActivationPage from '../pages/ActivationPage';
import PasswordResetPage from '../pages/PasswordResetPage'
import DashboardPage from '../pages/DashboardPage'
import UserListPage from '../pages/UserListPage'
import UserDetailsPage from '../pages/UserDetailsPage'
import NotFoundPage from '../pages/NotFoundPage'
import UserPasswordChangePage from '../pages/UserPasswordChangePage'
import UserDetailsEditPage from '../pages/UserDetailsEditPage'
import OwnDetailsPage from '../pages/OwnDetailsPage'
import OwnDetailsEditPage from '../pages/OwnDetailsEditPage'

const { ADMINISTRATOR, MODERATOR, CLIENT } = allroles;

export const routes = [
  { path: '/', component: HomePage, public: true },
  { path: '/login', component: LoginPage, public: true },
  { path: '/signup', component: SignupPage, public: true },
  { path: '/activation/:activationCode', component: ActivationPage, public: true },
  { path: '/password_reset', component: PasswordResetPage, public: true },
  { path: '/password_reset/:passwordResetCode', component: PasswordResetPage, public: true },
  { path: '/dashboard', component: DashboardPage, canAccess: [CLIENT, MODERATOR] },
  { path: '/users', component: UserListPage, canAccess: [ADMINISTRATOR] },
  { path: '/users/:userId', component: UserDetailsPage, canAccess: [ADMINISTRATOR] },
  { path: '/users/:userId/password_change', component: UserPasswordChangePage, canAccess: [ADMINISTRATOR] },
  { path: '/users/:userId/edit', component: UserDetailsEditPage, canAccess: [ADMINISTRATOR] },
  { path: '/my_profile', component: OwnDetailsPage},
  { path: '/my_profile/edit', component: OwnDetailsEditPage},
  { path: '*', component: NotFoundPage, public: true },
];