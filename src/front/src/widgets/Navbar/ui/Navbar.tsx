import React, { memo, useCallback, useState } from 'react';
import { Button, Layout, Row } from 'antd';
import cls from './Navbar.module.scss';

interface NavbarProps {
  className?: string;
}

export const Navbar = memo(({ className }: NavbarProps) => (
    <Layout.Header className={cls.headerNavbar}>
        <Row justify="start" align="middle">
            <Button className={cls.buttonNavbar}>Register</Button>
            <Button className={cls.buttonNavbar}>Login</Button>
        </Row>
    </Layout.Header>
));
