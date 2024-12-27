import React, { useState } from 'react'
import classNames from 'classnames';
import { Route, Switch } from 'react-router-dom';

import { ScrollPanel } from 'primereact/scrollpanel';

import AppTopbar from './../dashboard/AppTopbar';
import AppInlineProfile from './../dashboard/AppInlineProfile';
import AppMenu from './../dashboard/AppMenu';
import AppFooter from './../dashboard/AppFooter';

import Dashboard from './../dashboard/Dashboard';
import Expense from './../expense/Expense';
import EditExpense from './../expense/EditExpense';
import Income from './../income/Income';
import EditIncome from './../income/EditIncome';
import Profile from './../profile/Profile';
import EditProfile from './../profile/EditProfile';
import TransactionCalendar from './../calendar/TransactionCalendar';
import Analytics from './../analytics/Analytics';
import Setting from './../setting/Setting';
import ScrollToTop from './../dashboard/ScrollToTop';
import PageNotFound from './../errors/404';
import Budget from '../budget/Budget';

import { logout } from './../../Axios';
import { PrivateRoute } from './../../Routes';
import { useTracked } from './../../Store';
import EditBudget from '../budget/EditBudget';

const isDesktop = () => {
  return window.innerWidth > 1024;
};

const menu = [
  { label: 'Dashboard', url: '/dashboard', icon: 'pi pi-fw pi-home', command: () => { } },
  {
    label: 'Expense', url: '/expense', icon: 'pi pi-fw pi-dollar', command: () => { }
  },
  // items: [
  //   { label: 'Manage', url: '/expense', icon: 'pi pi-fw pi-plus', command: () => { } },
  // ]
  // },
  {
    label: 'Income', url: '/income', icon: 'pi pi-fw pi-money-bill', command: () => { }
  },
  //   items: [
  //     { label: 'Manage', url: '/income', icon: 'pi pi-fw pi-plus', command: () => { } },
  //   ]
  // },
  {
    label: 'Budget', url: '/budget', icon: 'pi pi-fw pi-briefcase', command: () => { }
  },
  { label: 'Calendar', url: '/calendar', icon: 'pi pi-fw pi-calendar', command: () => { } },
  { label: 'Analytics', url: '/analytics', icon: 'pi pi-fw pi-chart-bar', command: () => { } },
  { label: 'Settings', url: '/setting', icon: 'pi pi-fw pi-cog', command: () => { } },
  { label: 'Profile', url: '/profile', icon: 'pi pi-fw pi-user', command: () => { } },
  { label: 'Logout', url: '', icon: 'pi pi-fw pi-power-off', command: () => logout() },
];

const DashboardLayout = (props) => {

  const [state] = useTracked();

  const [staticMenuInactive, setStaticMenuInactive] = useState(false);
  const [overlayMenuActive, setOverlayMenuActive] = useState(false);
  const [mobileMenuActive, setMobileMenuActive] = useState(false);

  const onToggleMenu = () => {
    if (isDesktop()) {
      if (state.layoutMode === 'overlay') {
        setOverlayMenuActive(!overlayMenuActive);
      }
      else if (state.layoutMode === 'static') {
        setStaticMenuInactive(!staticMenuInactive);
      }
    }
    else {
      setMobileMenuActive(!mobileMenuActive)
    }
  }

  /**
   * If menu item has no child, this function will
   * close the menu on item click. Else it will
   * open the child drawer.
   */
  const onMenuItemClick = (event) => {
    if (!event.item.items) {
      setOverlayMenuActive(false);
      setMobileMenuActive(false);
    }
  }

  let logo = state.layoutColorMode === 'dark' ? require('./../../assets/logo-sidebar.png') : require('./../../assets/logo-sidebar.png');
  let wrapperClass = classNames('layout-wrapper', {
    'layout-overlay': state.layoutMode === 'overlay',
    'layout-static': state.layoutMode === 'static',
    'layout-static-sidebar-inactive': staticMenuInactive && state.layoutMode === 'static',
    'layout-overlay-sidebar-active': overlayMenuActive && state.layoutMode === 'overlay',
    'layout-mobile-sidebar-active': mobileMenuActive
  });
  let sidebarClassName = classNames("layout-sidebar", { 'layout-sidebar-dark': state.layoutColorMode === 'dark' });

  return (
    <div className={wrapperClass}>
      <AppTopbar onToggleMenu={onToggleMenu} />

      <div className={sidebarClassName}>
        <ScrollPanel style={{ height: '100%' }}>
          <div className="layout-sidebar-scroll-content">
            <div className="layout-logo">
              <img alt="Logo" src={logo} style={{ height: '60px' }} />
            </div>
            <AppInlineProfile />
            <AppMenu model={menu} onMenuItemClick={onMenuItemClick} />
          </div>
        </ScrollPanel>
      </div>
      <div className="layout-main" style={{ minHeight: '100vh', marginBottom: '-55px' }}>
        <Switch>
          <PrivateRoute exact strict path={'/dashboard'} component={Dashboard} />
          <PrivateRoute exact strict path={'/expense'} component={Expense} />
          <PrivateRoute exact strict path={'/expense/:expense_id/edit'} component={EditExpense} />
          <PrivateRoute exact strict path={'/income'} component={Income} />
          <PrivateRoute exact strict path={'/income/:income_id/edit'} component={EditIncome} />
          <PrivateRoute exact strict path={'/budget'} component={Budget} />
          <PrivateRoute exact strict path={'/budget/:budget_id/edit'} component={EditBudget} />
          <PrivateRoute exact strict path={'/calendar'} component={TransactionCalendar} />
          <PrivateRoute exact strict path={'/analytics'} component={Analytics} />
          <PrivateRoute exact strict path={'/setting'} component={Setting} />
          <PrivateRoute exact strict path={'/profile'} component={Profile} />
          <PrivateRoute exact strict path={'/profile/edit'} component={EditProfile} />
          <Route render={props => <PageNotFound {...props} />} />
        </Switch>
        <div style={{ height: '55px' }}>
          {/* For footer adjustment */}
        </div>
        <ScrollToTop />
      </div>
      <AppFooter />
      <div className="layout-mask" />
    </div>
  );
}

export default DashboardLayout;
