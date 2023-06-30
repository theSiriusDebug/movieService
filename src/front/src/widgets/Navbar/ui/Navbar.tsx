
import React, { memo, useCallback, useState } from 'react';
import cls from './Navbar.module.scss';
import { Button, Layout, Row } from 'antd';

interface NavbarProps {
  className?: string;
}

export const Navbar = memo(({ className }: NavbarProps) => {
    return (
        <Layout.Header className={cls.headerNavbar}>
            <Row justify="start" align="middle">
                <Button className={cls.buttonNavbar}>Register</Button>
                <Button className={cls.buttonNavbar}>Login</Button>
            </Row>
        </Layout.Header>
    );
});
