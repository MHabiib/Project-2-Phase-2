
import { createLocalVue, shallowMount } from '@vue/test-utils'
import Sidebar from '@/Components/Sidebar'
import Vuex from 'vuex'
import VueRouter from 'vue-router';


let wrapper
let store
let actions
let mutations
let state
let router = new VueRouter()
const localVue = createLocalVue()

localVue.use(VueRouter)
localVue.use(Vuex)

beforeEach(()  => {
    wrapper = shallowMount(Sidebar, {
        propsData: {},
        mocks: { $t: () => {} },
        stubs: {},
        store,
        localVue,   
        router, 
    });
})
afterEach(() => {
    wrapper.destroy()
})

describe('Components/Sidebar.vue',()=>{
    test('renders correctly', () => {
        expect(wrapper.element).toMatchSnapshot()
        expect(wrapper).toBeTruthy()
      })
      
    test('test function changeView', ()=>{
        wrapper.vm.$router.push = jest.fn()
        wrapper.vm.changeView()
        expect(wrapper.vm.$router.push).toBeCalled()
    })
    test('test function backtoLogin',()=>{
        wrapper.vm.$router.push = jest.fn()
        wrapper.vm.backToLogin()
        expect(wrapper.vm.$router.push).toBeCalled()
    })
    test('dashboard menu active',()=>{
        wrapper.setData({

        })
        wrapper.vm.changeView = jest.fn(()=>{
            wrapper.vm.isDashboard = true
        })
        const menu = wrapper.find('.dashBoard .menuWrapper')
        wrapper.vm.changeView('/dashboard')
        expect(wrapper.vm.changeView).toBeCalled()
        expect(wrapper.vm.isDashboard).toBe(true)
    })
    test('role admin\'s menu',()=>{
        expect(wrapper.text()).not.toContain('Management')

        wrapper.setData({
            isAdmin : true
        })
        expect(wrapper.text()).toContain('Management')
    })


})