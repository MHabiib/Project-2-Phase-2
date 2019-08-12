<template>
    <div class='sidebarComponent'>
        <div class="logoBlibli">
            <img src="../assets/logo-blibli.png" alt="Logo Blibli" class="logoBlibli">
        </div>

        <div class="menuContainer">
            <div class="menuParent activeMenu">
                <div class="menuWrapper">
                    <img src="../assets/dashboard-icon.png" alt="Dashboard" class='menuIcon'>
                    Dashboard
                </div>
            </div>
            
            <div class="menuParent">
                <div class="menuWrapper" @click="groupDetailExpand = true">
                    <img src="../assets/invoice-icon.png" alt="Dashboard" class='menuIcon'>
                    Group Detail
                </div>
                
                <div class="spacer" v-show="groupDetailExpand"></div>

                <div class="menuChild" v-show="groupDetailExpand">
                    Overview
                </div>

                <div class="menuChild" v-show="groupDetailExpand">
                    Expenses
                </div>

                <div class="menuChild" v-show="groupDetailExpand">
                    Members
                </div>
            </div>

            <div class="menuParent">
                <div class="menuWrapper">
                    <img src="../assets/invoice-icon.png" alt="Dashboard" class='menuIcon'>
                    Payment
                </div>
            </div>
        </div>
    </div>
</template>

<script>
document.title = 'Dashboard | Team Cash Flow Management'

export default {
    data() {
        return {
            groupDetailExpand: false
        }
    }
}
</script>

<style>
    .sidebarComponent {
        width: 280px;
        padding: 45px 0;
        background-color: var(--lightColor);
        min-height: 100vh;
        box-shadow: 5px 0 10px rgba(0, 0, 0, 0.16);
    }

    .logoBlibli {
        width: 178px;
        margin: auto;
        margin-bottom: 20px;
    }

    .menuParent {
        padding: 17px 0;
        box-sizing: border-box;
        cursor: pointer;
    }

    .menuIcon {
        width: 30px;
        margin-right: 10px;
    }

    .menuWrapper {
        display: flex;
        align-items: center;
        width: 160px;
        margin: auto;
        font-size: 14px;
        color: var(--darkColor);
        font-weight: 500;
    }

    .menuChild {
        color: var(--darkColor);
        font-size: 14px;
        padding: 12px 0;
        width: 50px;
        margin: auto;
    }

    .activeMenu {
        background-color: white;
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16);
    }

    .spacer {
        margin: 15px;
    }
</style>
