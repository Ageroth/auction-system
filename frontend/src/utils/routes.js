import ALLROLES from '../utils/allroles';
import HomePage from '../pages/HomePage'
import LoginPage from '../pages/LoginPage';
import SignupPage from '../pages/SignupPage'
import ActivationPage from '../pages/ActivationPage';
import PasswordResetPage from '../pages/PasswordResetPage'
import DashboardPage from '../pages/DashboardPage'
import UserListPage from '../pages/UserListPage'
import UserDetailsPage from '../pages/UserDetailsPage'
import NotFoundPage from '../pages/NotFoundPage'

const { ADMINISTRATOR, MODERATOR, CLIENT } = ALLROLES;

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
  { path: '*', component: NotFoundPage, public: true },
];