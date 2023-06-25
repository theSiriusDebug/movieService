import { StoreProvider } from './ui/StoreProvider';
import { AppDispatch, createReduxStore } from './config/store';
import type {
    StateSchema,
    ReduxStoreWithManager,
    ThunkExtraArg,
    ThunkConfig,
} from './config/StateSchema';

export {
    StoreProvider,
    createReduxStore,
    StateSchema,
    ReduxStoreWithManager,
    AppDispatch,
    ThunkExtraArg,
    ThunkConfig,
};
