import Cookie from 'js-cookie'
/**
 * 面包屑
 */
export default {
    state: {
        // 是否折叠，默认不折叠
        isCollapse: false,
        tabsList: [
            {
                path: '/',
                name: 'home',
                label: '首页',
                icon: 'home'
            },
        ],
        currentMenu: null,
        menu: [],
    },
    // 改变state的方法，mutations里的方法使用this.$store.commit('{xxx}')调用
    mutations: {
        collapseMenu(state) {
            state.isCollapse = !state.isCollapse
        },
        selectMenu(state, val) {
            if (val.name !== 'home') {
                state.currentMenu = val
                const result = state.tabsList.findIndex(item => item.name === val.name)
                state.menu = JSON.parse(Cookie.get('menu'))
                // 获取一级目录信息
                let parentMenu = state.menu.find(menu =>
                    menu.children && menu.children.some(child => child.name === val.name)
                )

                if (parentMenu) {
                    // 确认一级目录
                    parentMenu = {
                        path: parentMenu.path,
                        name: parentMenu.name,
                        label: parentMenu.label,
                        icon: parentMenu.icon
                    }

                    if (result === -1) {
                        // 如果没有找到二级目录，则添加一级和二级目录
                        state.tabsList = [
                            parentMenu,
                            val
                        ]
                    } else {
                        // 如果已经存在二级目录，则保留一级目录和二级目录
                        state.tabsList = [
                            parentMenu,
                            val
                        ]
                    }
                } else {
                    // 如果没有找到一级目录，则只显示首页和当前菜单项
                    state.tabsList = [
                        {
                            path: '/',
                            name: 'home',
                            label: '首页',
                            icon: 'home'
                        },
                        val
                    ]
                }
            } else {
                state.currentMenu = null
                state.tabsList = [
                    {
                        path: '/',
                        name: 'home',
                        label: '首页',
                        icon: 'home'
                    }
                ]
            }
        },
        closeTag(state, val) {
            const result = state.tabsList.findIndex(item => item.name === val.name)
            state.tabsList.splice(result, 1)
        },
        setMenu(state, val) {
            state.menu = val
            Cookie.set('menu', JSON.stringify(val))
        },
        getMenu(state, val) {
            return state.menu || Cookie.get('menu')
        },
        clearMenu(state) {
            state.menu = []
            Cookie.remove('menu')
        },
        addMenu(state, router) {
            if (!Cookie.get('menu')) {
                return
            }
            const menu = JSON.parse(Cookie.get('menu'))
            state.menu = menu
            console.log( router );
            const menuArray = []
            menu.forEach(item => {
                if (item.children) {
                    item.children = item.children.map(item => {
                        item.component = () => import(`@/views/${item.url}`)
                        return item
                    })
                    menuArray.push(...item.children)
                } else {
                    item.component = () => import(`@/views/${item.url}`)
                    menuArray.push(item)
                }
            })
            // 路由的动态添加
            menuArray.forEach(item => {
                router.addRoute('Main', item)
            })
            
        }
    }
}
