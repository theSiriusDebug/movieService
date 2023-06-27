
import React, { memo, useCallback, useState } from 'react';
import cls from './Navbar.module.scss';
import { Button} from 'antd';


interface NavbarProps {
  className?: string;
}

export const Navbar = memo(({ className }: NavbarProps) => {
    return (
      <header>
        <Button className={cls.NavButton}>Register</Button>
        <Button className={cls.NavButton}>Login</Button>
      </header>
    )

});
